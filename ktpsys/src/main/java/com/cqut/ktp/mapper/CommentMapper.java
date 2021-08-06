package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface CommentMapper {
    /**
     * 评论
     *
     * @param comment
     */
    void comment(@Param("comment") Comment comment);

    /**
     * ]
     * 获取当前作业的所有评论
     *
     * @param tno
     * @return
     */
    List<Comment> getComments(@Param("tno") String tno);
}
