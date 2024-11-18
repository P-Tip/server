package com.ptip.service;

import com.ptip.mapper.CourseMapper;
import com.ptip.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public  List<Course> searchCourse(String title, String professor, String courseNo, String classroom, String major, String grade, List<String> credits, List<String> courseTypes, List<Character> days, List<String> periods) {
        return courseMapper.selectCourse(title, professor, courseNo, classroom, major, grade, credits, courseTypes, days, periods);
    }
}
