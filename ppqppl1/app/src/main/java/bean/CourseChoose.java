package bean;

public class CourseChoose {
    public int id;
    public String stuid;
    public String courseid;
    public double grade = -1;

    public CourseChoose() {
    }

    public CourseChoose(int id, String stu_id, String course_id, double grade) {
        this.id = id;
        this.stuid = stu_id;
        this.courseid = course_id;
        this.grade = grade;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
