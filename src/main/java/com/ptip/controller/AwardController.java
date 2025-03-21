package com.ptip.controller;

import com.ptip.dto.CustomPageResponse;
import com.ptip.models.Department;
import com.ptip.models.Program;
import com.ptip.service.AwardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/award")
@Tag(name = "Award", description = "장학금 관련 API")
public class AwardController {

    @Autowired
    private AwardService awardService;

    @Operation(summary = "ID로 프로그램 검색", description = "프로그램 ID를 기반으로 프로그램을 검색합니다. 빈칸이면 모든 프로그램 출력")
    @GetMapping("")
    public List<Program> searchById(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = false) Integer id) {
        return awardService.searchById(id);
    }

    @Operation(summary = "초성으로 부서 필터링", description = "부서명의 초성을 기준으로 부서를 필터링합니다. 빈칸이면 모든 부서 출력")
    @GetMapping("/filter")
    public List<Department> filterByFirstConsonant(
            @Parameter(description = "검색할 초성", required = true)
            @RequestParam("consonant") String consonant) {
        return awardService.filterByFirstConsonant(consonant);
    }

    // 통합된 검색 및 필터링 API
    @Operation(summary = "프로그램 통합 검색", description = "이름, 포인트, 부서를 기준으로 프로그램을 검색합니다.")
    @GetMapping("/search")
    public List<Program> searchPrograms(
            @Parameter(description = "프로그램 이름")
            @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "포인트")
            @RequestParam(value = "point", required = false) Integer point,
            @Parameter(description = "부서명")
            @RequestParam(value = "department", required = false) String department
    ) {
        return awardService.searchPrograms(name, point, department);
    }

    @Operation(summary = "프로그램을 목록 검색 (페이지네이션)",
            description = "검색어(query)와 정렬 방식(order)으로 프로그램을 검색하며, 페이지네이션을 적용하여 데이터를 반환합니다." +
                    "정렬 방식은 point_asc,point_desc")
    @GetMapping("/searchP")
    public ResponseEntity<CustomPageResponse<Program>> searchWithPaging(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String order            // 정렬 방식 추후 정리 예정
    ){
        CustomPageResponse<Program> response = awardService.searchWithPaging(query,page,order);
        return ResponseEntity.ok(response);

    }
}
