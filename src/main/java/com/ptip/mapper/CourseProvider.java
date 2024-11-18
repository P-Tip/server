package com.ptip.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

public class CourseProvider {
    public String selectCourseDynamic(@Param("title") String title,
                                      @Param("professor") String professor,
                                      @Param("courseNo") String courseNo,
                                      @Param("classroom") String classroom,
                                      @Param("major") String major,
                                      @Param("grade") String grade,
                                      @Param("credits") List<String> credits,
                                      @Param("courseTypes") List<String> courseTypes,
                                      @Param("days") List<Character> days,
                                      @Param("periods") List<String> periods) {
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
            if (classroom != null && !classroom.isEmpty()) {
                WHERE("REPLACE(classroom, ' ', '') LIKE CONCAT('%', REPLACE(#{classroom}, ' ', ''), '%')");
            }
            if (major != null) {
                WHERE("name = #{major}");
            }
            if (grade != null) {
                WHERE("grade <= #{grade}");
            }
            if (credits != null && !credits.isEmpty()) {
                if (credits.contains("4")) {
                    WHERE("(credit > 4 OR credit IN (" + String.join(",", credits) + "))");
                } else {
                    WHERE("credit IN (" + String.join(",", credits) + ")");
                }
            }
            if (courseTypes != null && !courseTypes.isEmpty()) {
                WHERE("course_type IN (" + String.join(",", courseTypes.stream().map(s -> "'" + s + "'").collect(Collectors.toList())) + ")");
            }
            if (days != null && !days.isEmpty() && periods != null && !periods.isEmpty()) {
                StringBuilder timeCondition = new StringBuilder("(");

                for (int i = 0; i < days.size(); i++) {
                    if (i > 0) {
                        timeCondition.append(" OR ");
                    }
                    timeCondition.append("(course_time LIKE CONCAT(#{days[").append(i).append("]}, '%') ")
                                 .append("AND #{periods[").append(i).append("]} LIKE CONCAT('%', REPLACE(substring(course_time, 3), ',', ''), '%'))");
                }
                timeCondition.append(")");
                WHERE(timeCondition.toString());
            }
        }}.toString();
    }
}

