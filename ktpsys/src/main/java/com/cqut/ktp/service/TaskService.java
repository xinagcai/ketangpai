package com.cqut.ktp.service;


import com.cqut.ktp.pojo.Task;

import java.util.List;

/**
 * @author 陈佳铭
 */
public interface TaskService {
    /**
     * 查询作业
     *
     * @param tno
     * @param cno
     * @return
     */
    List<Task> selectTasks(String tno, String cno);

    /**
     * 注册
     *
     * @param task
     */
    void insertTask(Task task);

    /**
     * 修改作业信息
     *
     * @param task
     */
    void updateTask(Task task);

    /**
     * 删除作业
     *
     * @param tno
     */
    void deleteTask(String tno);
}
