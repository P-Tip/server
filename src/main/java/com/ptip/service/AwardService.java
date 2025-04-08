package com.ptip.service;

import com.ptip.dto.CustomPageResponse;
import com.ptip.dto.LikedProgramsRequestDto;
import com.ptip.mapper.AwardMapper;
import com.ptip.models.Department;
import com.ptip.models.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardService {

    @Autowired
    private final AwardMapper awardMapper;

    public AwardService(AwardMapper awardMapper) {
        this.awardMapper = awardMapper;
    }

    public List<Program> searchById(Integer id) {
        return awardMapper.searchById(id);
    }

    public List<Department> filterByFirstConsonant(String consonant) {
        return awardMapper.filterByFirstConsonant(consonant);
    }

    public List<Program> searchPrograms(String name, Integer point, String department) {
        return awardMapper.searchPrograms(name, point, department);
    }

    public CustomPageResponse<Program> searchWithPaging(String query, int page, String order){
        int size = 10;
        int offset = page * size;  //sql 쿼리에서 어디서부터 가져올지 시작점(?)

        String sortColumn = "id";
        String sortDirection = "ASC";

        // 정렬 방식에 따라 기준 컬럼과 방향을 결정
        if (order.equalsIgnoreCase("point_desc")) {
            sortColumn = "max_point";
            sortDirection = "DESC";
        } else if (order.equalsIgnoreCase("point_asc")) {
            sortColumn = "min_point";
            sortDirection = "ASC";
        } else if (order.equalsIgnoreCase("end_date")) {
            sortColumn = "end_date";
            sortDirection = "";
        }
        System.out.println("정렬 기준: " + sortColumn + ", 방향: " + sortDirection);
        List<Program> programs = awardMapper.searchWithPaging(query, offset, size, sortColumn, sortDirection);
        int total = awardMapper.countPrograms(query);
        int totalPages = (int) Math.ceil((double) total / size);
        boolean last = (page+1) >= totalPages;
        return  new CustomPageResponse<>(programs,total,totalPages,last);
    }

    public String toggleLike(int userId, int programId) {
        boolean exists = awardMapper.existsLike(userId, programId);
        if (exists) {
            awardMapper.deleteLike(userId, programId);
            return "unliked";
        } else {
            awardMapper.insertLike(userId, programId);
            return "liked";
        }
    }

    public List<Integer> getLikedProgramIds(int userId) {
        return awardMapper.selectLikedProgramIds(userId);
    }

    public String addLikedPrograms(int userId, LikedProgramsRequestDto requestDto) {
        for(int likedProgram : requestDto.getLikedPrograms()){
            boolean exists = awardMapper.existsLike(userId, likedProgram);
            if (exists) {
                continue;
            }
            awardMapper.insertLike(userId, likedProgram);
        }
        return "성공적으로 좋아요한 프로그램들을 추가하였습니다.";
    }

}
