package com.ptip.mapper;

import com.ptip.models.Program;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProgramMapper {

    //id 조회
    @Select("SELECT p.id, p.program_name, p.contents, " +
            "       COALESCE(p.min_point, '미정') AS min_point, " +
            "       COALESCE(p.max_point, '미정') AS max_point, " +
            "       p.department_name, d.internal_num, p.link " +
            "FROM program p " +
            "JOIN department d ON p.department_name = d.department_name " +
            "WHERE (p.id = #{id} OR #{id} IS NULL)" +
            "ORDER BY p.id")
    List<Program> searchById(@Param("id") Integer id);

    // 통합 검색(MyBatis용 SQL문)
    @Select("<script>" +
            "SELECT p.id, p.program_name, p.contents, " +
            "       COALESCE(p.min_point, '미정') AS min_point, " +
            "       COALESCE(p.max_point, '미정') AS max_point, " +
            "       p.department_name, d.internal_num, p.link " +
            "FROM program p " +
            "JOIN department d ON p.department_name = d.department_name " +
            "WHERE 1=1 " +
            "<if test='name != null'> AND p.program_name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='point != null'> AND p.max_point >= #{point}</if> " +
            "<if test='department != null'> AND p.department_name = #{department}</if> " +
            "ORDER BY p.id" +
            "</script>")
    List<Program> searchPrograms(@Param("name") String name,
                                 @Param("point") Integer point,
                                 @Param("department") String department);
}
