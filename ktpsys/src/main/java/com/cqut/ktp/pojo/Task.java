package com.cqut.ktp.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 陈佳铭
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    /**
     * 作业号
     */
    private String tno;
    /**
     * 课程号
     */
    private String cno;
    /**
     * 课程名
     */
    private String tname;
    /**
     * 课程描述
     */
    private String description;
    /**
     * 附件
     */
    private String annex;
    /**
     * 平均分
     */
    @JsonAlias({"avg_score"})
    private Float avgScore;
    /**
     * 通过率
     */
    @JsonAlias({"pass_rate"})
    private Float passRate;
    /**
     * 最高分
     */
    @JsonAlias({"highest_score"})
    private Integer highestScore;
    /**
     * 最低分
     */
    @JsonAlias({"lowest_score"})
    private Integer lowestScore;
    /**
     * 创建日期
     */
    @JsonAlias({"create_date"})
    private Date createDate;
    /**
     * 截止日期
     */
    @JsonAlias({"end_date"})
    private Date endDate;
    /**
     * 提交数量
     */
    @JsonAlias({"submit_num"})
    private Integer submitNum;
    /**
     * 批改数量
     */
    @JsonAlias({"judge_num"})
    private Integer judgeNum;
}
