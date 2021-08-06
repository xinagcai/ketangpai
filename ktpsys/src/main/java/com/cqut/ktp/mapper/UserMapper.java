package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface UserMapper {
    /**
     * 登录验证或注册验证可用
     *
     * @param account
     * @return
     */
    User userVerify(@Param("account") String account);

    /**
     * 通过uid获取个人信息
     *
     * @param uid
     * @return
     */
    User findEntityByUid(@Param("uid") String uid);

    /**
     * 查询某门课程的学生
     *
     * @param cno
     * @return
     */
    List<User> selectStudents(@Param("cno") String cno);

    /**
     * 查询某门课程的助教教师
     *
     * @param cno
     * @return
     */
    List<User> selectTeacherAss(@Param("cno") String cno);

    /**
     * 查询课程的拥有者
     *
     * @param cno
     * @return
     */
    User selectTeach(@Param("cno") String cno);

    /**
     * 注册
     *
     * @param user
     */
    void register(@Param("user") User user);

    /**
     * 修改用户信息
     *
     * @param password
     * @param pid
     * @param name
     * @param school
     * @param identifyNo
     * @param userId
     */
    void updateUserInfo(@Param("password") String password, @Param("permission") Integer pid,
                        @Param("name") String name, @Param("school") Integer school,
                        @Param("identifyNo") String identifyNo, @Param("userId") String userId);
}
