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

    @GetMapping("")
    public List<Program> searchById(@RequestParam(value = "id", required = false) Integer id) {
        return programService.searchById(id);
    }

    // 통합된 검색 및 필터링 API
    @GetMapping("/search")
    public List<Program> searchPrograms(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "point", required = false) Integer point,
            @RequestParam(value = "department", required = false) String department
    ) {
        return programService.searchPrograms(name, point, department);
    }
}
