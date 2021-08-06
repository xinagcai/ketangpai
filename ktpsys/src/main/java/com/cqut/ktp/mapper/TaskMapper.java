package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 陈佳铭、欧阳建荣
 */
@Mapper
public interface TaskMapper {
    /**
     * 查询作业
     *
     * @param tno
     * @param cno
     * @return
     */
    List<Task> selectTasks(@Param("tno") String tno, @Param("cno") String cno);

    /**
     * 通过tno查询课程
     *
     * @param tno
     * @return
     */
    Course selectTaskByTno(@Param("tno") String tno);

    /**
     * 注册
     *
     * @param task
     */
    void insertTask(@Param("task") Task task);

    /**
     * 修改作业信息
     *
     * @param task
     */
    void updateTask(@Param("task") Task task);

    /**
     * 删除作业
     *
     * @param tno
     */
    void deleteTask(@Param("tno") String tno);

    /**
     * 批改学生作业
     *
     * @param tno
     */
    void revise(@Param("tno") String tno);

    /**
     * 学生提交作业后修改对应的提交作业数量
     *
     * @param tno
     */
    void submit(@Param("tno") String tno);
}
