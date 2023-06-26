package com.example.ppqppl1_server.entity;

import javax.persistence.*;

@Entity
@Table(name="Teacher")
public class Teacher {
    @Id
    public String id;          // 教师id
    public String name;     // 姓名
    public String phonenum;   // 电话号码
    public String pwd;

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
