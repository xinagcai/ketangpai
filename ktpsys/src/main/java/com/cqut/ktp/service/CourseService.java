package com.cqut.ktp.service;

import com.cqut.ktp.pojo.Course;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface CourseService {
    /**
     * 添加新课程
     *
     * @param course
     * @return
     */
    Boolean addCourse(Course course);

    /**
     * 更新数据
     *
     * @param name
     * @param year
     * @param semester
     * @param num
     * @param archive
     * @param deleteStatus
     * @param cno
     * @return
     */
    Boolean updateCourse(String name, String year, String semester, Integer num, Integer archive, Integer deleteStatus, String cno);

    /**
     * 获取某教师的课程
     *
     * @param uid
     * @return
     */
    List<Course> getCourses(String uid);

    /**
     * 获取某一课程的成员数
     *
     * @param cno
     * @return
     */
    Integer getMembers(String cno);

    /**
     * 通过cno获取一门课程的信息
     *
     * @param cno
     * @return
     */
    Course getCourseByCno(String cno);
}
