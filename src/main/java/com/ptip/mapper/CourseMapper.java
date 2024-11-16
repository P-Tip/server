package com.ptip.mapper;

import com.ptip.models.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
                              @Param("major") String major,
                              @Param("day") List<Character> day,
                              @Param("period") List<String> period);
}
