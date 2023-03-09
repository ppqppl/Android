package com.example.ppqpplthird;

public class Stu {

    String name;
    String pro;

    public void Student(){}
    public void Student(String n,String p){
        this.name = n;
        this.pro = p;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getPro() {
        return pro;
    }
    public String getName() {
        return name;
    }
}