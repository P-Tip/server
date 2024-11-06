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

    // 학수번호로 검색
    @GetMapping("/search/courseNo")
    public List<Course> getByCourseNo(@RequestParam("courseNo") String course_no) {
        return courseService.searchByCourseNo(course_no);
    }

    // 강의명으로 검색
    @GetMapping("/search/title")
    public List<Course> getByTitle(@RequestParam("title") String title) {
        return courseService.searchByTitle(title);
    }

    // 전공/영역으로 필터링
    @GetMapping("/filter")
    public List<Course> getByCourseType(
            @RequestParam("courseType") String course_type,
            @RequestParam(value = "major", required = false) String major) {
        if (course_type.equals("교양")) {
            return courseService.filterByCourseType();
        } else {
            return courseService.filterByMajor(major);
        }
    }
}
