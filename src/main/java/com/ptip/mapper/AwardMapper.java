package com.ptip.mapper;

import com.ptip.models.Department;
import com.ptip.models.Program;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AwardMapper {

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

    //부서 조회
    @Select("<script>" +
            "SELECT department_name, location, internal_num " +
            "FROM department " +
            "<if test='consonant != null and consonant != \"\"'>" +
            "WHERE CASE " +
            "    WHEN LEFT(department_name, 1) BETWEEN '가' AND '깋' THEN 'ㄱ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '나' AND '닣' THEN 'ㄴ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '다' AND '딯' THEN 'ㄷ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '라' AND '맇' THEN 'ㄹ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '마' AND '밓' THEN 'ㅁ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '바' AND '빟' THEN 'ㅂ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '사' AND '싷' THEN 'ㅅ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '아' AND '잏' THEN 'ㅇ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '자' AND '짛' THEN 'ㅈ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '차' AND '칳' THEN 'ㅊ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '카' AND '킿' THEN 'ㅋ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '타' AND '팋' THEN 'ㅌ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '파' AND '핗' THEN 'ㅍ' " +
            "    WHEN LEFT(department_name, 1) BETWEEN '하' AND '힣' THEN 'ㅎ' " +
            "    ELSE '' END = #{consonant} " +
            "</if>" +
            "</script>")
    List<Department> filterByFirstConsonant(@Param("consonant") String consonant);

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




    @Select("<script>" +
            "SELECT \n" +
            "    p.id,\n" +
            "    p.program_name,\n" +
            "    p.contents,\n" +
            "    CAST(COALESCE(p.min_point, -1) AS SIGNED) AS min_point,\n" +
            "    CAST(COALESCE(p.max_point, -1) AS SIGNED) AS max_point,\n" +
            "    p.department_name,\n" +
            "    d.internal_num,\n" +
            "    CASE \n" +
            "        WHEN p.link IS NULL OR p.link = '' THEN d.info_link\n" +
            "        ELSE p.link\n" +
            "    END AS link\n" +
            "FROM program p\n" +
            "JOIN department d ON p.department_name = d.department_name\n" +
            "<where>\n" +
            "    <if test=\"query != null and query != ''\">\n" +
            "        (\n" +
            "            p.program_name LIKE CONCAT('%', #{query}, '%')\n" +
            "            OR p.contents LIKE CONCAT('%', #{query}, '%')\n" +
            "            OR p.department_name LIKE CONCAT('%', #{query}, '%')\n" +
            "        )\n" +
            "    </if>\n" +
            "</where>\n" +
            "<choose>\n" +
            "    <when test=\"sortColumn != null and sortDirection != null\">\n" +
            "        ORDER BY ${sortColumn} ${sortDirection}\n" +
            "    </when>\n" +
            "    <otherwise>\n" +
            "        ORDER BY p.id ASC\n" +
            "    </otherwise>\n" +
            "</choose>\n" +
            "LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<Program> searchWithPaging(
            @Param("query") String query,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection
    );


    @Select("<script>" +
            "SELECT COUNT(*)\n" +
            "    FROM program p\n" +
            "    JOIN department d ON p.department_name = d.department_name\n" +
            "    <where>\n" +
            "        <if test=\"query != null and query != ''\">\n" +
            "            (\n" +
            "                p.program_name LIKE CONCAT('%', #{query}, '%')\n" +
            "                OR p.contents LIKE CONCAT('%', #{query}, '%')\n" +
            "                OR p.department_name LIKE CONCAT('%', #{query}, '%')\n" +
            "            )\n" +
            "        </if>\n" +
            "    </where>" +
            "</script>")
    int countPrograms(@Param("query") String query);
}
