package com.ptip.controller;

import com.ptip.models.Department;
import com.ptip.models.Program;
import com.ptip.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/award")
public class AwardController {

    @Autowired
    private AwardService awardService;

    @GetMapping("")
    public List<Program> searchById(@RequestParam(value = "id", required = false) Integer id) {
        return awardService.searchById(id);
    }

    @GetMapping("/filter")
    public List<Department> filterByFirstConsonant(@RequestParam("consonant") String consonant) {
        return awardService.filterByFirstConsonant(consonant);
    }

    // 통합된 검색 및 필터링 API
    @GetMapping("/search")
    public List<Program> searchPrograms(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "point", required = false) Integer point,
            @RequestParam(value = "department", required = false) String department
    ) {
        return awardService.searchPrograms(name, point, department);
    }
}
