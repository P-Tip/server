package com.ptip.mapper;

import com.ptip.models.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE REPLACE(course_no, '-', '') LIKE CONCAT('%', REPLACE(#{courseNo}, ' ', ''), '%')")
    List<Course> selectByCourseNo(@Param("courseNo") String courseNo);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE TITLE LIKE CONCAT('%', REPLACE(#{title}, ' ', ''), '%')")
    List<Course> selectByTitle(@Param("title") String title);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE PROFESSOR LIKE CONCAT('%', REPLACE(#{professor}, ' ', ''), '%')")
    List<Course> selectByProfessor(@Param("professor") String professor);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE CREDIT = #{credit}")
    List<Course> selectByCredit(@Param("credit") int credit);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE grade = #{grade}")
    List<Course> selectByGrade(@Param("grade") int grade);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE (course_time LIKE CONCAT(#{day}, '%')) AND (SUBSTR(course_time, 3, 1) BETWEEN #{startTime} AND #{endTime}) AND (RIGHT(course_time, 1) BETWEEN #{startTime} AND #{endTime})")
    List<Course> selectByTime(@Param("day") String day, @Param("startTime") int startTime, @Param("endTime") int endTime);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE course_type LIKE #{courseType}")
    List<Course> selectByCourseType(@Param("courseType") String courseType);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE name LIKE CONCAT('%', #{major}, '%')")
    List<Course> selectByMajor(@Param("major") String major);
}
