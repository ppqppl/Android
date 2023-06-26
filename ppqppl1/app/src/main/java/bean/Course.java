package bean;

public class Course {
    public String id;       // 课程编号
    public String name;     // 课程名
    public String teaid;   // 教师对应id

    public Course(String id, String name, String teaid) {
        this.id = id;
        this.name = name;
        this.teaid = teaid;
    }

    public Course() {
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

    public String getTeaid() {
        return teaid;
    }

    public void setTea_id(String tea_id) {
        this.teaid = tea_id;
    }
}
