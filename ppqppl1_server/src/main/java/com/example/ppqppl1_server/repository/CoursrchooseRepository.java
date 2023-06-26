package com.example.ppqppl1_server.repository;

import com.example.ppqppl1_server.entity.CourseChoose;
import com.example.ppqppl1_server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CoursrchooseRepository extends JpaRepository<CourseChoose,String> {

    public List<CourseChoose> findByStuidAndCourseid(@Param("courseid") String course_id,@Param("stuid") String stu_id);

    public List<CourseChoose> deleteByStuidAndCourseid(@Param("courseid") String course_id,@Param("stuid") String stu_id);

    public List<CourseChoose> findByStuid(@Param("stuid") String stu_id);

    public List<CourseChoose> findByCourseid(@Param("courseid") String course_id);

    @Transactional
    public List<CourseChoose> deleteByStuid(@Param("stuid") String stuid);

    @Transactional
    public List<CourseChoose> deleteByCourseid(@Param("courseid") String courseid);
}
