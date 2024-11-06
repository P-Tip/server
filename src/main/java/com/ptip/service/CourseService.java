package com.ptip.service;

import com.ptip.mapper.CourseMapper;
import com.ptip.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    // 학수번호로 검색
    public List<Course> searchByCourseNo(String course_no) {
        return courseMapper.selectByCourseNo(course_no);
    }

    // 강의명으로 검색
    public List<Course> searchByTitle(String title) {
        return courseMapper.selectByTitle(title);
    }

    // 전공/영역으로 필터링
    public List<Course> filterByCourseType() {
        return courseMapper.selectByCourseType();
    }

    // 전공/영역으로 필터링
    public List<Course> filterByMajor(String major) {
        return courseMapper.selectByMajor(major);
    }
}
