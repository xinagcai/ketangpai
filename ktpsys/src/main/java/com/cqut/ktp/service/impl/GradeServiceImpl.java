package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.CourseMapper;
import com.cqut.ktp.mapper.GradeMapper;
import com.cqut.ktp.mapper.MessageMapper;
import com.cqut.ktp.mapper.TaskMapper;
import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.pojo.Grade;
import com.cqut.ktp.pojo.Message;
import com.cqut.ktp.pojo.User;
import com.cqut.ktp.service.GradeService;
import com.cqut.ktp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

/**
 * @author 欧阳建荣
 */
@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private MessageService messageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean revise(String uid, String tno, Integer score) {
        Object savePoint = null;
        try {
            // 设置回滚点
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

            gradeMapper.revise(uid, tno, score);
            afterRevise(uid, tno, score);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reviseMulti(Map<String, Object> map) {
        Object savePoint = null;
        try {
            // 设置回滚点
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
            List<String> members = (List<String>) map.get("member");
            List<Integer> scores = (List<Integer>) map.get("score");

            for (int i = 0; i < members.size(); i++) {
                gradeMapper.revise(members.get(i), (String) map.get("tno"), scores.get(i));

                afterRevise(members.get(i), (String) map.get("tno"), scores.get(i));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reviseMany(List<String> member, String tno, Integer score) {
        Object savePoint = null;
        try {
            // 设置回滚点
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

            gradeMapper.reviseMany(member, tno, score);
            for (String uid : member) {
                afterRevise(uid, tno, score);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submit(String uid, String tno, String answer) {
        Object savePoint = null;
        try {
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

            gradeMapper.submit(uid, tno, answer);
            gradeMapper.changeToSubmitStatus(uid, tno);
            // 党作业是未提交的情况下，才将该作业提交的数量+1
            if (gradeMapper.getGrade(uid, tno).getScore() == -1) {
                taskMapper.submit(tno);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return false;
        }
    }

    @Override
    public List<Grade> getGradeList(String tno) {
        return gradeMapper.getGradeList(tno);
    }

    @Override
    public List<Grade> getScoreList(String uid, String cno) {
        return gradeMapper.getScoreList(uid, cno);
    }

    @Override
    public Grade getGrade(String uid, String tno) {
        return gradeMapper.getGrade(uid, tno);
    }

    /**
     * 批改之后的发送消息
     *
     * @param uid
     * @param tno
     * @param score
     */
    private void afterRevise(String uid, String tno, Integer score) {
        // 存放作业对应的课程
        Course course = taskMapper.selectTaskByTno(tno);
        User from = new User();
        User to = new User();
        from.setUserId(course.getAdmin().getUserId());
        to.setUserId(uid);
        String msg;

        // 首次批改
        if (gradeMapper.getGrade(uid, tno).getStatus() != 1) {
            taskMapper.revise(tno);

            msg = "课程: " + course.getName() + ", 已经批改完成，分数: " + score;
        } else {
            // 修改之前的成绩
            msg = "课程: " + course.getName() + ", 修改了你的分数，新的分数: " + score;
        }

        messageService.send(new Message(null,
                from,
                to,
                msg,
                null));
    }
}
