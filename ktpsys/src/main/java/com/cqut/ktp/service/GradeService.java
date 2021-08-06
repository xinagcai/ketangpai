package com.cqut.ktp.service;

import com.cqut.ktp.pojo.Grade;

import java.util.List;
import java.util.Map;

/**
 * @author 欧阳建荣
 */
public interface GradeService {
    /**
     * 教师批改作业
     *
     * @param uid
     * @param tno
     * @param score
     * @return
     */
    Boolean revise(String uid, String tno, Integer score);

    /**
     * 统一给分
     *
     * @param member
     * @param tno
     * @param score
     * @return
     */
    Boolean reviseMany(List<String> member, String tno, Integer score);

    /**
     * 随机给分
     *
     * @param map
     * @return
     */
    Boolean reviseMulti(Map<String, Object> map);

    /**
     * 提交作业
     *
     * @param uid
     * @param tno
     * @param answer
     * @return
     */
    Boolean submit(String uid, String tno, String answer);

    /**
     * 获取某次的作业的学生提交情况
     *
     * @param tno
     * @return
     */
    List<Grade> getGradeList(String tno);

    /**
     * 获取某一位学生在这门课程的成绩
     *
     * @param uid
     * @param cno
     * @return
     */
    List<Grade> getScoreList(String uid, String cno);

    /**
     * 获取某一位学生的作业情况
     *
     * @param uid
     * @param tno
     * @return
     */
    Grade getGrade(String uid, String tno);
}
