package com.ptip.models;

public record Program(
        int id,
        String programName,
        String contents,
        String minpoint,
        String maxpoint,
        String department_name,
        String internalNum,
        String link
) {
}