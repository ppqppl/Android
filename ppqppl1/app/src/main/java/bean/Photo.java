package bean;

import android.graphics.Bitmap;

public class Photo {
    private int id;
    private String fileName;
    private Bitmap bitmap = null;
    private String info;
    private String filepath;

    public Photo(){};

    public Photo(int id,String fileName,String info,String filepath){
        this.id = id;
        this.fileName = fileName;
        this.info = info;
        this.filepath = filepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
