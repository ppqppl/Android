package com.example.ppqppl1_server.controller;

import com.alibaba.fastjson.JSON;
import com.example.ppqppl1_server.entity.Course;
import com.example.ppqppl1_server.entity.Student;
import com.example.ppqppl1_server.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping("/subject")
    public List<Course> subject(){
        return courseRepository.findAll();
    }

    @RequestMapping("/find_course_by_teaid")
    public List<Course> findByTeaid(@RequestParam("courseid")String str){
        String tea_id = (String) JSON.parse(str);
        System.out.println(tea_id);
        return courseRepository.findByTeaid(tea_id);
    }

    @RequestMapping("/addcourse")
    @ResponseBody
    public String add(@RequestParam("course")String str){
        Course course = JSON.parseObject(str,Course.class);
        courseRepository.save(course);
        return "添加成功！";
    }

    @PostMapping(value = "/delcourse")
    @ResponseBody
    public String deletestu(@RequestParam("course")String str){
        Course cc = JSON.parseObject(str,Course.class);
        courseRepository.delete(cc);
        return "删除成功";
    }
}
