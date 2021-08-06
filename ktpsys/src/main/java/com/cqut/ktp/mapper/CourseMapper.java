package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface CourseMapper {
    /**
     * 创建新课程
     *
     * @param course 课程
     */
    void addCourse(@Param("course") Course course);

    /**
     * 修改课程的信息
     *
     * @param name         课程名
     * @param year         学年
     * @param semester     学期
     * @param num          成员数量
     * @param archive      归档状态
     * @param deleteStatus 删除状态
     */
    void updateCourse(@Param("name") String name, @Param("year") String year,
                      @Param("semester") String semester, @Param("num") Integer num,
                      @Param("archive") Integer archive, @Param("cdelete") Integer deleteStatus,
                      @Param("cno") String cno);

    /**
     * 查询所有没被删除的课程
     *
     * @param uid 用户唯一id
     * @return 一个课程列表
     */
    List<Course> getCourses(@Param("admin") String uid);

    /**
     * 获取某一课程的所有学生
     *
     * @param cno 课程号
     * @return 成员数，不包括教师
     */
    Integer getMembers(@Param("cno") String cno);

    /**
     * 通过cno获取一门课程的信息
     *
     * @param cno
     * @return
     */
    Course getCourseByCno(@Param("cno") String cno);
}
