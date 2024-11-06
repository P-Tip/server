package com.ptip.mapper;


import com.ptip.models.Haksik;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HaksikMapper {
    @Select("SELECT * FROM test_db.haksik WHERE date = #{date} ")
    List<Haksik> getHaksikByDate(@Param("date") String date);

    @Select("SELECT * FROM test_db.haksik WHERE #{startdate} <= date && date <= #{enddate} ORDER BY date ASC")
    List<Haksik> getHaksikByRange(@Param("startdate") String startdate, @Param("enddate") String enddate);
}
