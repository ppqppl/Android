package com.example.ppqppl1_server.entity;

import javax.persistence.*;

@Entity
@Table(name="coursechoose")
public class CourseChoose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String stuid;
    public String courseid;
    public double grade = -1;

    public CourseChoose() {
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
