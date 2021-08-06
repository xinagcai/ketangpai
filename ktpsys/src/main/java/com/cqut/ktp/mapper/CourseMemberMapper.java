package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.pojo.CourseMember;
import com.cqut.ktp.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface CourseMemberMapper {
    /**
     * 加入课程
     *
     * @param cno
     * @param uid
     */
    void addMember(@Param("cno") String cno, @Param("uid") String uid);

    /**
     * 更新人数
     *
     * @param cno
     */
    void updateMember(@Param("cno") String cno);

    /**
     * 有学生退课的时候调用该方法
     *
     * @param cno
     */
    void minusMember(@Param("cno") String cno);

    /**
     * 退课或者归档
     *
     * @param cno
     * @param uid
     * @param status
     */
    void changeStatus(@Param("cno") String cno, @Param("uid") String uid, @Param("status") String status);

    /**
     * 获取某门课程的成员
     *
     * @param cno
     * @return
     */
    List<User> getCourseMembers(@Param("cno") String cno);

    /**
     * 获取某位学生或助教所加入的课程
     *
     * @param uid
     * @return
     */
    List<Course> getCourse(@Param("uid") String uid);

    /**
     * 获取某门课程的成员数量，包括教师
     *
     * @param cno
     * @return
     */
    Integer getMemberCount(@Param("cno") String cno);

    /**
     * 查询该学生是否是退课之后再次选择该课程
     *
     * @param cno
     * @param uid
     * @return
     */
    CourseMember getCourseMember(@Param("cno") String cno, @Param("uid") String uid);

    /**
     * 在课程信息改变是，修改所有的学生选课信息
     *
     * @param cno
     * @param status
     */
    void changeAllStatus(@Param("cno") String cno, @Param("status") String status);
}
