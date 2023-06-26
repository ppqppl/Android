package com.example.ppqppl1_server.controller;

import com.alibaba.fastjson.JSON;
import com.example.ppqppl1_server.entity.Course;
import com.example.ppqppl1_server.entity.CourseChoose;
import com.example.ppqppl1_server.entity.Student;
import com.example.ppqppl1_server.entity.Teacher;
import com.example.ppqppl1_server.repository.CoursrchooseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseChooseController {
    @Autowired
    private CoursrchooseRepository coursrchooseRepository;

    @RequestMapping("/courseChoose")
    public List<CourseChoose> courseChoose(){
        return coursrchooseRepository.findAll();
    }

    @RequestMapping("/find_CourseChoose_ByStu_id")
    public List<CourseChoose> findByStu_id(@RequestParam("str")String str){
        String stu_id = (String) JSON.parse(str);
        return coursrchooseRepository.findByStuid(stu_id);
    }

    @RequestMapping("/find_CourseChoose_ByCourse_id")
    public List<CourseChoose> findByCourseid(@RequestParam("courseid")String str){
        String course_id = (String) JSON.parse(str);
        return coursrchooseRepository.findByCourseid(course_id);
    }

    @RequestMapping("/find_by_Stuid_and_Courseid")
    public List<CourseChoose> findByStu_idAndCourse_id(@RequestParam("CourseChoose")String str){
        CourseChoose courseChoose = JSON.parseObject(str,CourseChoose.class);
        return coursrchooseRepository.findByStuidAndCourseid(courseChoose.courseid,courseChoose.stuid);
    }

    @RequestMapping("/addCoursechose")
    public String add(@RequestParam("courseChose")String str){
        CourseChoose courseChoose = JSON.parseObject(str,CourseChoose.class);
        coursrchooseRepository.save(courseChoose);
        return "添加成功";
    }

    @RequestMapping("/delete_by_Stuid_and_Courseid")
    public String deleteByStuidAndCourseid(@RequestParam("courseChose")String str){
        System.out.println(str);
        CourseChoose courseChoose = JSON.parseObject(str,CourseChoose.class);
        coursrchooseRepository.delete(courseChoose);
        return "删除成功";
    }

    @RequestMapping("/deletecc_by_Stuid")
    public String deleteByStuid(@RequestParam("stuid")String str){
        System.out.println(str);
        String stuid = (String) JSON.parse(str);
        if(coursrchooseRepository.findByStuid(stuid) == null || coursrchooseRepository.findByStuid(stuid).size() == 0){
            return "This Name doesn't exsist in table!";
        }
        coursrchooseRepository.deleteByStuid(stuid);
        return "删除成功";
    }

    @RequestMapping("/deletecc_by_cid")
    public String deleteByCourseid(@RequestParam("courseid")String str){
        System.out.println(str);
        String courseid = (String) JSON.parse(str);
        coursrchooseRepository.deleteByCourseid(courseid);
        return "删除成功";
    }
}
