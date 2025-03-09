package com.ptip.mapper;

import com.ptip.models.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (email, password, role) VALUES (#{email}, #{password}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
}
