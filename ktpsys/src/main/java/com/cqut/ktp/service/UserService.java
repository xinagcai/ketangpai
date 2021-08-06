package com.cqut.ktp.service;

import com.cqut.ktp.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface UserService {
    /**
     * 登录验证或注册验证可用
     *
     * @param account
     * @return
     */
    User userVerify(String account);

    /**
     * 查询某一课程所有的成员或者某一身份的成员
     *
     * @param cno
     * @return
     */
    List<User> selectUsers(String cno);

    /**
     * 注册
     *
     * @param user
     */
    void register(User user);

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
    void updateUserInfo(String password, Integer pid, String name, Integer school, String identifyNo, String userId);

    /**
     * 通过uid获取个人信息
     *
     * @param uid
     * @return
     */
    User findEntityByUid(String uid);
}
