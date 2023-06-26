package com.example.ppqppl1_server.controller;

import com.alibaba.fastjson.JSON;
import com.example.ppqppl1_server.entity.Course;
import com.example.ppqppl1_server.entity.Student;
import com.example.ppqppl1_server.entity.Teacher;
import com.example.ppqppl1_server.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepository;

    @RequestMapping("/teacher")
    public List<Teacher> teacher(){
        return teacherRepository.findAll();
    }

    @RequestMapping("/addtea")
    @ResponseBody
    public String add(@RequestParam("tea")String str){
        Teacher tea = JSON.parseObject(str,Teacher.class);
        teacherRepository.save(tea);
        return "添加成功！";
    }

    @RequestMapping("/findbyteaid")
    @ResponseBody
    public Teacher findById(@RequestParam("teaid")String str){
        String id = (String) JSON.parse(str);
        System.out.println(id);
        return teacherRepository.findById(id).orElse(null);
    }
}
