package com.cqut.ktp.service;

import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.pojo.User;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface CourseMemberService {
    /**
     * 加入课程，与更新人数一起使用
     *
     * @param cno
     * @param uid
     * @return
     */
    Boolean addMember(String cno, String uid);

    /**
     * 退课或者归档
     *
     * @param cno
     * @param uid
     * @param status
     * @return
     */
    Boolean changeStatus(String cno, String uid, String status);

    /**
     * 成员管理中删除学生
     *
     * @param members
     * @return
     */
    Boolean deleteMembers(String cno, List<User> members);

    /**
     * 在课程信息改变是，修改所有的学生选课信息
     *
     * @param cno
     * @param status
     * @return
     */
    Boolean changeAllStatus(String cno, String status);

    /**
     * 获取某门课程的成员
     *
     * @param cno
     * @return
     */
    List<User> getCourseMembers(String cno);

    /**
     * 获取某门课程的成员数量，包括教师
     *
     * @param uid
     * @return
     */
    List<Course> getCourse(String uid);

    /**
     * 获取某门课程的成员数量，包括教师
     *
     * @param cno
     * @return
     */
    Integer getMemberCount(String cno);
}
