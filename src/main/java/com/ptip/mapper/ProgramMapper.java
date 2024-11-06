package com.ptip.mapper;

import com.ptip.models.Program;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProgramMapper {

    // 1. 프로그램명으로 검색 (NULL 값일 경우 "미정"으로 대체)
    @Select("SELECT p.id, p.program_name, p.contents, " +
            "COALESCE(p.min_point, '미정') AS min_point, " +
            "COALESCE(p.max_point, '미정') AS max_point, " +
            "p.department_name, d.internal_num, p.link " +
            "FROM program p " +
            "JOIN department d ON p.department_name = d.department_name " +
            "WHERE p.program_name LIKE CONCAT('%', #{programName}, '%')")
    List<Program> searchByProgramName(@Param("programName") String programName);

    // 2. 포인트 필터링 (NULL 값일 경우 "미정"으로 대체)
    @Select("SELECT p.id, p.program_name, p.contents, " +
            "COALESCE(p.min_point, '미정') AS min_point, " +
            "COALESCE(p.max_point, '미정') AS max_point, " +
            "p.department_name, d.internal_num, p.link " +
            "FROM program p " +
            "JOIN department d ON p.department_name = d.department_name " +
            "WHERE #{point} BETWEEN p.min_point AND p.max_point")
    List<Program> filterByPoint(@Param("point") int point);
}
