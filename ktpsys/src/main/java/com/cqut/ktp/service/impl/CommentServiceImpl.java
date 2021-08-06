package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.CommentMapper;
import com.cqut.ktp.pojo.Comment;
import com.cqut.ktp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 欧阳建荣
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Boolean comment(Comment comment) {
        try {
            comment.setCmDate(new Date());

            commentMapper.comment(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comment> getComments(String tno) {
        return commentMapper.getComments(tno);
    }
}
