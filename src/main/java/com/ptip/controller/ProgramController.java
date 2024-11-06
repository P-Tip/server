package com.ptip.controller;

import com.ptip.models.Program;
import com.ptip.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/award")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    // 프로그램명을 통한 검색
    @GetMapping("/program/search")
    public List<Program> searchByProgramName(@RequestParam("name") String programName) {
        return programService.searchByProgramName(programName);
    }

    // 포인트 필터링
    @GetMapping("/program/filter")
    public List<Program> filterByPoint(@RequestParam("point") int point) {
        return programService.filterByPoint(point);
    }
}
