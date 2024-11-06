package com.ptip.service;

import com.ptip.mapper.ProgramMapper;
import com.ptip.models.Program;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ProgramService {

    @Autowired
    private ProgramMapper programMapper;

    // 프로그램명으로 검색
    public List<Program> searchByProgramName(String programName) {
        return programMapper.searchByProgramName(programName);
    }

    // 포인트 범위로 필터링
    public List<Program> filterByPoint(int point) {
        return programMapper.filterByPoint(point);
    }
}
