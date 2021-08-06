package com.cqut.ktp.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 欧阳建荣
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * 登录账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 权限
     */
    @JsonAlias({"pid", "permission"})
    private Permission permission;
    /**
     * 姓名
     */
    private String name;
    /**
     * 学校
     */
    private School school;
    /**
     * 学号or教师号
     */
    @JsonAlias({"i_no", "identifyNo", "ino", "identify"})
    private String identifyNo;
    /**
     * 跳转用个人id
     */
    @JsonAlias({"uid", "u_id", "userId"})
    private String userId;

    /**
     * 头像地址
     */
    private String avatar;
}
