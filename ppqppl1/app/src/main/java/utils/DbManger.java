package utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import bean.Photo;
import bean.User;
import bean.Constant;

public class DbManger {
    private static SqLiteHelper helper;
    public static SqLiteHelper getIntance(Context context){
        if (helper==null){
            helper=new SqLiteHelper(context);
        }
        return helper;
    }
    public static void execSQL(SQLiteDatabase db, String sql){
        if(db!=null){
            if (sql!=null && !"".equals(sql)){
                db.execSQL(sql);
            }
        }
    }


    public static List<Photo> ImgcursorToList(Cursor cursor){
        List<Photo> list=new LinkedList<>();
        //moveToNext() ---返回为true则还有记录，为false则已经读取完记录
        while (cursor.moveToNext()){
//                 int columnIndex=cursor.getColumnIndex(Constant.ID);
//                 int id=cursor.getInt(columnIndex);
            int id = cursor.getInt((int)cursor.getColumnIndex(Constant.ID));
            String fileName=cursor.getString((int)cursor.getColumnIndex(Constant.FILENAME));
            String info=cursor.getString((int)cursor.getColumnIndex(Constant.INFO));
            String filepath=cursor.getString((int)cursor.getColumnIndex(Constant.FILEPATH));
            Photo user=new Photo(id,fileName,info,filepath);
            list.add(user);
        }
        return list;
    }
    public static Cursor ImgselectDataBySql(SQLiteDatabase db, String sql, String[] selectionArgs){
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(sql,selectionArgs);
        }
        return cursor;
    }

    /**
     * 语句查询获得Cursor对象
     * @param db 数据库对象
     * @param sql 查询sql语句
     * @param selectionArgs 查询的条件的占位符
     * @return 查询结果
     */
    public static Cursor UserselectDataBySql(SQLiteDatabase db, String sql, String[] selectionArgs){
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(sql,selectionArgs);
        }
        return cursor;
    }

    /**
     * 将出查询的Cursor对象转换成List集合
     * @param cursor 游标对象
     * @return 集合对象
     */
    public static List<User> UsercursorToList(Cursor cursor){
        List<User> list=new LinkedList<>();
        //moveToNext() ---返回为true则还有记录，为false则已经读取完记录
        while (cursor.moveToNext()){
//                 int columnIndex=cursor.getColumnIndex(Constant.ID);
//                 int id=cursor.getInt(columnIndex);
            String account=cursor.getString((int)cursor.getColumnIndex(Constant.ACCOUNT));
            String pwd=cursor.getString((int)cursor.getColumnIndex(Constant.PWD));
            String name=cursor.getString((int)cursor.getColumnIndex(Constant.NAME));
            String phonenum=cursor.getString((int)cursor.getColumnIndex(Constant.PHONENUM));
            String ID=cursor.getString((int)cursor.getColumnIndex(Constant.ID_ACC));
            String authority=cursor.getString((int)cursor.getColumnIndex(Constant.AUTHORITY));
            User data=new User(account,pwd,name,phonenum,ID,authority);
            list.add(data);
        }
        return list;
    }
}
