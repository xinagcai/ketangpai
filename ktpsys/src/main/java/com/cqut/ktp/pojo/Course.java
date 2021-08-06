package com.cqut.ktp.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 欧阳建荣
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    /**
     * 课程号，5位UUID
     */
    private String cno;

    /**
     * 课程名
     */
    @JsonAlias({"cname"})
    private String name;

    /**
     * 创建者
     */
    private User admin;

    /**
     * 创建时间，yyyy-mm-dd hh:mm:ss
     */
    private Date createDate;

    /**
     * 学年
     */
    private String year;

    /**
     * 学期 1 or 2
     */
    private String semester;

    /**
     * 成员数量
     */
    private Integer members;

    /**
     * 归档状态 0->正常情况，1->归档
     */
    private Integer archiveStatus;

    /**
     * 删除状态 0->未删除，1->删除
     */
    private Integer deleteStatus;
}
