package com.ptip.models;

public record Course(
        String course_no,
        String title,
        String course_type,
        int credit,
        char pass,
        int grade,
        String course_time,
        String classroom,
        String major,
        String professor
){
}