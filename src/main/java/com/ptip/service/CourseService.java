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

    public  List<Course> searchCourse(String title, String professor, String courseNo, String credit, String grade, String courseType, String major, List<Character> day, List<String> period) {
        return courseMapper.selectCourse(title, professor, courseNo, credit, grade, courseType, major, day, period);
    }
}
