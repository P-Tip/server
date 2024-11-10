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

    // 학수번호 검색
    public List<Course> searchByCourseNo(String courseNo) {
        return courseMapper.selectByCourseNo(courseNo);
    }

    // 강의명 검색
    public List<Course> searchByTitle(String title) {
        return courseMapper.selectByTitle(title);
    }

    // 교수명 검색
    public List<Course> searchByProfessor(String professor) {
        return courseMapper.selectByProfessor(professor);
    }

    // 해당 학점으로 찾기
    public  List<Course> findByCredit(int credit) {
        return courseMapper.selectByCredit(credit);
    }

    // 해당 학년으로 찾기
    public  List<Course> findByGrade(int grade) {
        return courseMapper.selectByGrade(grade);
    }

    // 강의 시간으로 찾기
    public  List<Course> findByTime(String day, int startTime, int endTime) {
        return courseMapper.selectByTime(day, startTime, endTime);
    }

    // 전공/영역으로 필터링
    public List<Course> filterByCourseType(String courseType) {
        return courseMapper.selectByCourseType(courseType);
    }

    // 전공/영역으로 필터링
    public List<Course> filterByMajor(String major) {
        return courseMapper.selectByMajor(major);
    }
}
