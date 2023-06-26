package com.example.ppqppl1_server.repository;

import com.example.ppqppl1_server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String> {

    public List<Student> findByName(@Param("name") String name);

    public List<Student> findByNameAndSex(@Param("name") String name, @Param("sex") String sex);

    public List<Student> findByNameLike(@Param("name") String name);

    @Transactional
    public List<Student> deleteByName(@Param("name") String name);

}
