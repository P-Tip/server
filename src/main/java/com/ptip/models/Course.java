package com.ptip.models;

public class Course {

    private String course_no;
    private String title;
    private String course_type;
    private int credit;
    private int grade;
    private String course_time;
    private String classroom;
    private String major;
    private String professor;

    public Course(String course_no, String title, String course_type, int credit, int grade, String course_time, String classroom, String major, String professor) {
        this.course_no = course_no;
        this.title = title;
        this.course_type = course_type;
        this.credit = credit;
        this.grade = grade;
        this.course_time = course_time;
        this.classroom = classroom;
        this.major = major;
        this.professor = professor;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
