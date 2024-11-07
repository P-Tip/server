package com.ptip.mapper;

import com.ptip.models.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("SELECT course_no, title, course_type, course_time, classroom, name as major, professor " +
            "FROM course_schedule.course join course_schedule.major on course.major_id = major.id " +
            "WHERE REPLACE(course_no, '-', '') LIKE CONCAT('%', REPLACE(#{course_no}, ' ', ''), '%')")
    List<Course> selectByCourseNo(@Param("course_no") String course_no);

    @Select("SELECT course_no, title, course_type, course_time, classroom, name as major, professor " +
            "FROM course_schedule.course join course_schedule.major on course.major_id = major.id " +
            "WHERE TITLE LIKE CONCAT('%', REPLACE(#{title}, ' ', ''), '%')")
    List<Course> selectByTitle(@Param("title") String title);

    @Select("SELECT course_no, title, course_type, course_time, classroom, name as major, professor " +
            "FROM course_schedule.course left join course_schedule.major on course.major_id = major.id " +
            "WHERE course_type LIKE CONCAT('%', '교', '%')")
    List<Course> selectByCourseType();

    @Select("SELECT course_no, title, course_type, course_time, classroom, name as major, professor " +
            "FROM course join major on course.major_id = major.id " +
            "WHERE name LIKE CONCAT('%', #{major}, '%')")
    List<Course> selectByMajor(@Param("major") String major);
}
