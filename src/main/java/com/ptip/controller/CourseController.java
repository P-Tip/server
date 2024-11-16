package com.ptip.controller;

import com.ptip.models.Course;
import com.ptip.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    public List<Course> getCourse(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "professor", required = false) String professor,
            @RequestParam(value = "courseNo", required = false) String courseNo,
            @RequestParam(value = "credit", required = false) String credit,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "courseType", required = false) String courseType,
            @RequestParam(value = "major", required = false) String major,
            @RequestParam(value = "times", required = false) List<String> times) {

        List<Character> day = new ArrayList<>();
        List<String> period = new ArrayList<>();
        System.out.println(times);
        if (times != null) {
            for (String time : times) {
                day.add(time.charAt(0));
                if (time.length() == 1) {
                    period.add("123456789");
                } else {
                    period.add(time.substring(1));
                }
            }
        }

        return courseService.searchCourse(title, professor, courseNo, credit, grade, courseType, major, day, period);
    }
}
