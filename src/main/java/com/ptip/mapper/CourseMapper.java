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
                              @Param("classroom") String classroom,
                              @Param("major") String major,
                              @Param("grade") String grade,
                              @Param("credits") List<String> credits,
                              @Param("courseTypes") List<String> courseTypes,
                              @Param("days") List<Character> days,
                              @Param("periods") List<String> periods);
}
