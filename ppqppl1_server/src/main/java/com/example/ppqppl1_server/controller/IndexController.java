package com.example.ppqppl1_server.controller;

import com.example.ppqppl1_server.entity.Student;
import com.example.ppqppl1_server.repository.StudentRepository;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("")
    public String student(ModelMap map){
        List<Student> students = studentRepository.findAll();
        map.addAttribute("students",students);
        map.addAttribute("stu1",new Student());
        return "index";
    }

    @PostMapping(value = "/find_by_id")
    @ResponseBody
    public Student findByID(Student stu1){
        Student stu = studentRepository.findById(stu1.stu_id).orElse(null);
        return stu;
    }

//    @RequestMapping("/add")
//    @ResponseBody
//    public String save(Student stu1){
//        studentRepository.save(stu1);
//        return "Add Success!";
//    }

    @RequestMapping("/add")
//    @ResponseBody
    public String save(Student stu1){
        studentRepository.save(stu1);
        return "redirect: ";
    }

    @PostMapping(value = "/delete_by_id")
    @ResponseBody
    public String deleteByID(Student stu1){
        if(studentRepository.findById(stu1.stu_id).orElse(null) == null){
            return "This ID doesn't exsist in table!";
        }
        studentRepository.deleteById(stu1.stu_id);
        return "Delete Success!";
    }

    @PostMapping(value = "/find_by_name")
    @ResponseBody
    public List<Student> findByName(Student stu1){
        return studentRepository.findByName(stu1.name);
    }

    @PostMapping(value = "/delete_by_name")
    @ResponseBody
    public String deleteByName(Student stu1){
        if(studentRepository.findByName(stu1.name) == null || studentRepository.findByName(stu1.name).size() == 0){
            return "This Name doesn't exsist in table!";
        }
        studentRepository.deleteByName(stu1.name);
        return "Delete Success!";
    }

    @PostMapping(value = "/find_by_name_and_sex")
    @ResponseBody
    public List<Student> findByNameAndSex(Student stu1){
        return studentRepository.findByNameAndSex(stu1.name,stu1.sex);
    }

    @PostMapping(value = "/find_by_name_like")
    @ResponseBody
    public List<Student> findByNameLike(Student stu1){
        return studentRepository.findByNameLike("%"+ stu1.name+"%");
    }
}
