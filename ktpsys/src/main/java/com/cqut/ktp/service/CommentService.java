package com.cqut.ktp.service;

import com.cqut.ktp.pojo.Comment;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface CommentService {
    /**
     * 评论
     *
     * @param comment
     * @return
     */
    Boolean comment(Comment comment);

    /**
     * 获取所有的评论
     *
     * @param tno
     * @return
     */
    List<Comment> getComments(String tno);
}
