package com.example.ppqppl1_server.repository;

import com.example.ppqppl1_server.entity.Course;
import com.example.ppqppl1_server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,String> {
    public List<Course> findByTeaid(@Param("teaid") String name);
}
