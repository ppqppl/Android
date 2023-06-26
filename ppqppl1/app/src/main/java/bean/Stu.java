package bean;

public class Stu {

    String name;
    String pro;

    public Stu() {
    }

    public Stu(String name, String pro) {
        this.name = name;
        this.pro = pro;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }
}