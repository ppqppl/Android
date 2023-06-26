package com.example.ppqppl1_server.entity;

import javax.persistence.*;

@Entity
@Table(name="student")
public class Student {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    public String stu_id;           // 学生号
    public String name;             // 姓名
    public int age;                 // 年龄
    public String sex;              // 性别
    public String phoneNumber;      // 手机号
    public String trainDate;        // 入学日期
    public String modifyDateTime;   // 信息创建时间
    public String major;            // 专业
    public String class_num;        // 班级
    public String pwd;              // 密码 账号是id
    public int ifchosecourse = 0;   // 是否选课

    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClass_num() {
        return class_num;
    }

    public void setClass_num(String class_num) {
        this.class_num = class_num;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getIfchosecourse() {
        return ifchosecourse;
    }

    public void setIfchosecourse(int ifchosecourse) {
        this.ifchosecourse = ifchosecourse;
    }
}
