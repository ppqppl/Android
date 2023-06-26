package com.example.ppqppl1_server.controller;

import com.alibaba.fastjson.JSON;
import com.example.ppqppl1_server.entity.Student;
import com.example.ppqppl1_server.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/student")
    public List<Student> student(){
        return studentRepository.findAll();

    }

    @RequestMapping("/addstu")
    @ResponseBody
    public String add(@RequestParam("stu")String str){
        Student stu = JSON.parseObject(str,Student.class);
        studentRepository.save(stu);
        return "添加成功！";
    }

    @PostMapping(value = "/delstu")
    @ResponseBody
    public String deletestu(@RequestParam("stu")String str){
        Student stu = JSON.parseObject(str,Student.class);
        studentRepository.delete(stu);
        return "删除成功";
    }

    @RequestMapping("/findbyid")
    @ResponseBody
    public Student findById(@RequestParam("id")String str){
        String id = (String) JSON.parse(str);
        System.out.println(id);
        return studentRepository.findById(id).orElse(null);
    }

    @PostMapping(value = "/find_stu_by_name_like")
    @ResponseBody
    public List<Student> findByNameLike(@RequestParam("name")String str){
        String name = (String) JSON.parse(str);
        return studentRepository.findByNameLike("%"+ name+"%");
    }

}
