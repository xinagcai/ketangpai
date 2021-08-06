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
public class CourseMember {
    /**
     * 所属课程
     */
    private Course course;

    /**
     * 学生或助教
     */
    private User user;

    /**
     * 课程状态
     */
    private String status;
}
