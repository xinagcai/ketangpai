package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface GradeMapper {
    /**
     * 在教师布置完作业后，在数据库中添加该位学生的提交情况
     *
     * @param uid
     * @param tno
     */
    void addGrade(@Param("uid") String uid, @Param("tno") String tno);

    /**
     * 教师批改作业
     *
     * @param uid
     * @param tno
     * @param score
     */
    void revise(@Param("uid") String uid, @Param("tno") String tno,
                @Param("score") Integer score);

    /**
     * 统一给分
     *
     * @param member
     * @param tno
     * @param score
     */
    void reviseMany(@Param("member") List<String> member, @Param("tno") String tno,
                    @Param("score") Integer score);

    /**
     * 提交作业
     *
     * @param uid
     * @param tno
     * @param answer
     */
    void submit(@Param("uid") String uid, @Param("tno") String tno,
                @Param("answer") String answer);

    /**
     * 提交作业后改变为已提交状态
     *
     * @param uid
     * @param tno
     */
    void changeToSubmitStatus(@Param("uid") String uid, @Param("tno") String tno);

    /**
     * 获取某次的作业的学生提交情况，未提交的可以不用显示
     *
     * @param tno
     * @return
     */
    List<Grade> getGradeList(@Param("tno") String tno);

    /**
     * 获取某一位学生在这门课程的成绩
     *
     * @param uid
     * @param cno
     * @return
     */
    List<Grade> getScoreList(@Param("uid") String uid, @Param("cno") String cno);

    /**
     * 获取某位学生的成绩信息
     *
     * @param uid
     * @param tno
     * @return
     */
    Grade getGrade(@Param("uid") String uid, @Param("tno") String tno);
}
