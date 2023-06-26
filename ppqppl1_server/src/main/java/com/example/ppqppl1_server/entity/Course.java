package com.example.ppqppl1_server.entity;

import javax.persistence.*;

@Entity
@Table(name="course")
public class Course {
    @Id
    public String id;          // 课程编号
    public String name;     // 课程名
    public String teaid;   // 教师对应id

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

    public void setTeaid(String teaid) {
        this.teaid = teaid;
    }
}
