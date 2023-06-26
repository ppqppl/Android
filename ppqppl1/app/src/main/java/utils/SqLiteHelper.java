package utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bean.Constant;

public class SqLiteHelper extends SQLiteOpenHelper {
    public SqLiteHelper(Context context){
        super(context,"Mydb.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_user = "create table "+ Constant.TABLE_NAME_ACC +"(account varchar primary key,pwd varchar,name varchar,phonenum varchar,id varchar,authority varchar)";
        String sql_img = "create table "+ Constant.TABLE_NAME +"(id integer primary key autoincrement,fileName varchar,info varchar,filepath varchar)";
        db.execSQL(sql_user);
        db.execSQL(sql_img);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
