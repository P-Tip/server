package com.ptip.controller;

import com.ptip.models.Course;
import com.ptip.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 검색유형으로 과목명, 교수명, 학수번호 중 하나를 골라서 검색
    @GetMapping("/search")
    public List<Course> getByCourseNo(@RequestParam("searchType") String searchType, @RequestParam("input") String input) {
        if (searchType.equals("과목명")) {
            return courseService.searchByTitle(input);
        } else if (searchType.equals("교수명")) {
            return courseService.searchByCourseNo(input);
        } else if (searchType.equals("학수번호")) {
            return courseService.searchByProfessor(input);
        } else {
            throw new IllegalArgumentException("searchType parameter is required one of the following: 과목명, 교수명, 학수번호.");
        }
    }

    // 해당 학점으로 찾기
    @GetMapping("/find/credit")
    public List<Course> getByCredit(@RequestParam("credit") int credit) {
        return courseService.findByCredit(credit);
    }

    // 해당 학년으로 찾기
    @GetMapping("/find/grade")
    public List<Course> getByGrade(@RequestParam("grade") int grade) {
        return courseService.findByGrade(grade);
    }

    // 강의 시간으로 찾기
    @GetMapping("/find/time")
    public List<Course> getByGrade(@RequestParam("day") String day, @RequestParam("startTime") int startTime, @RequestParam("endTime") int endTime) {
        return courseService.findByTime(day, startTime, endTime);
    }

    // 전공/영역으로 필터링
    @GetMapping("/filter")
    public List<Course> getByCourseType(
            @RequestParam("courseType") String courseType,
            @RequestParam(value = "major", required = false) String major) {
        if (courseType.equals("전공")) {
            return courseService.filterByMajor(major);
        } else {
            return courseService.filterByCourseType(courseType);
        }
    }
}
