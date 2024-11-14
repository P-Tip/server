package com.ptip.models;

public record Program(
        int id,
        String programName,
        String contents,
        String min_point,
        String max_point,
        String department_name,
        String internalNum,
        String link
) {
}