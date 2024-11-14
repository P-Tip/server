package com.ptip.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class CourseProvider {
    public String selectCourseDynamic(@Param("title") String title,
                                      @Param("professor") String professor,
                                      @Param("courseNo") String courseNo,
                                      @Param("credit") String credit,
                                      @Param("grade") String grade,
                                      @Param("courseType") String courseType,
                                      @Param("major") String major) {
        return new SQL() {{
            SELECT("course_no, title, course_type, credit, pass, grade, course_time, classroom, name AS major, professor");
            FROM("course_schedule.course");
            LEFT_OUTER_JOIN("course_schedule.major ON course.major_id = major.id");

            if (title != null && !title.isEmpty()) {
                WHERE("REPLACE(TITLE, ' ', '') LIKE CONCAT('%', REPLACE(#{title}, ' ', ''), '%')");
            }
            if (professor != null && !professor.isEmpty()) {
                WHERE("professor LIKE CONCAT('%', #{professor}, '%')");
            }
            if (courseNo != null && !courseNo.isEmpty()) {
                WHERE("REPLACE(course_no, '-', '') LIKE CONCAT('%', REPLACE(#{courseNo}, ' ', ''), '%')");
            }
            if (credit != null) {
                WHERE("credit = #{credit}");
            }
            if (grade != null) {
                WHERE("grade = #{grade}");
            }
            if (courseType != null) {
                if (courseType.equals("전공")) {
                    WHERE("name = #{major}");
                } else {
                    WHERE("course_type = #{courseType}");
                }
            }
        }}.toString();
    }
}

