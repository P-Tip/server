package com.ptip.mapper;

import com.ptip.models.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface CourseMapper {

    @SelectProvider(type = CourseProvider.class, method = "selectCourseDynamic")
    List<Course> selectCourse(@Param("title") String title,
                              @Param("professor") String professor,
                              @Param("courseNo") String courseNo,
                              @Param("credit") String credit,
                              @Param("grade") String grade,
                              @Param("courseType") String courseType,
                              @Param("major") String major);

    @Select("SELECT course_no, title, course_type, credit, grade, course_time, classroom, name AS major, professor " +
            "FROM course_schedule.course LEFT JOIN course_schedule.major ON course.major_id = major.id " +
            "WHERE (course_time LIKE CONCAT(#{day}, '%')) AND (SUBSTR(course_time, 3, 1) BETWEEN #{startTime} AND #{endTime}) AND (RIGHT(course_time, 1) BETWEEN #{startTime} AND #{endTime})")
    List<Course> selectByTime(@Param("day") String day, @Param("startTime") int startTime, @Param("endTime") int endTime);

}
