package com.cqut.ktp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 欧阳建荣
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    /**
     * 用户
     */
    private User user;

    /**
     * 作业
     */
    private Task task;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 批改状态
     */
    private Integer status;

    /**
     * 提交的作业
     */
    private String answer;

}
