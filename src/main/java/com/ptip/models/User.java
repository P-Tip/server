package com.ptip.models;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("User")
public class User {
    private Long id;
    private String email;
    private String password;
    private String role;
}