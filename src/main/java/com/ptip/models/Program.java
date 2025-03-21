package com.ptip.models;

public record Program(
        int id,
        String programName,
        String contents,
        int min_point,
        int max_point,
        String department_name,
        String internalNum,
        String link
) {
}