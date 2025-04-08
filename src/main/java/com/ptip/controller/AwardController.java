package com.ptip.controller;

import com.ptip.auth.service.TokenService;
import com.ptip.dto.CustomPageResponse;
import com.ptip.dto.LikedProgramsRequestDto;
import com.ptip.models.Department;
import com.ptip.models.Program;
import com.ptip.service.AwardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/award")
@Tag(name = "Award", description = "장학금 관련 API")
public class AwardController {

    @Autowired
    private AwardService awardService;
    @Autowired
    private TokenService tokenService;

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
            description = "검색어(query)와 정렬 방식(order)으로 프로그램을 검색하며, 페이지네이션을 적용하여 데이터를 반환합니다.\n" +
                    "정렬 방식은 point_asc(포인트 오름차순), point_desc(포인트 내림차순), end_date(마감일순) 세가지가 있습니다.")
    @GetMapping("/searchP")
    public ResponseEntity<CustomPageResponse<Program>> searchWithPaging(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String order            // 정렬 방식 추후 정리 예정
    ){
        CustomPageResponse<Program> response = awardService.searchWithPaging(query,page,order);
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "좋아요 토글", description = "좋아요 상태를 토글합니다. 이미 좋아요한 경우는 취소하고, 아니라면 등록합니다.")
    @PostMapping("/like/{programId}")
    public ResponseEntity<Map<String, String>> toggleLike(
            @PathVariable("programId") int programId,
            @RequestHeader("Authorization") String token) {

        int userId = tokenService.getUserIdFromToken(token);  // 토큰에서 userId 추출
        String status = awardService.toggleLike(userId, programId);

        Map<String, String> response = new HashMap<>();
        response.put("status", status);  // liked or unliked
        response.put("message", status.equals("liked") ? "좋아요 완료" : "좋아요 취소 완료");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저가 좋아요한 프로그램 목록 조회", description = "특정 유저가 좋아요 누른 프로그램들의 ID 목록을 반환합니다.")
    @GetMapping("/likes")
    public ResponseEntity<List<Integer>> getLikedProgramIds(
            @RequestHeader("Authorization") String token) {
        int userId = tokenService.getUserIdFromToken(token);
        return ResponseEntity.ok(awardService.getLikedProgramIds(userId));
    }

    @Operation(summary = "유저가 좋아요한 프로그램들 한번에 추가", description = "유저가 좋아요를 누른 프로그램들을 리스트 형식으로 받아서 DB에 저장합니다.")
    @PostMapping("/like/add")
    public ResponseEntity<?> addLikedPrograms(
            @RequestHeader("Authorization") String token,
            @RequestBody LikedProgramsRequestDto requestDto
    ){
        int userId = tokenService.getUserIdFromToken(token);
        return ResponseEntity.ok(awardService.addLikedPrograms(userId, requestDto));
    }

}
