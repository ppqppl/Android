package bean;

public class Teacher {
    public String id;          // 教师id
    public String name;     // 姓名
    public String phonenum;   // 电话号码
    public String pwd;

    public Teacher(String id, String name, String phonenum, String pwd) {
        this.id = id;
        this.name = name;
        this.phonenum = phonenum;
        this.pwd = pwd;
    }

    public Teacher() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
