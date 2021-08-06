package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface MessageMapper {
    /**
     * 查询未阅读数量
     *
     * @param to 接收方
     * @return
     */
    Integer getNotReadMsgCount(@Param("to") String to);

    /**
     * 查询接收方的消息，可以是所有，也可以是未读和已读
     *
     * @param to
     * @param status 为空或者-1就是读取所有消息
     * @return
     */
    List<Message> getMessages(@Param("to") String to, @Param("status") Integer status);

    /**
     * 发送消息
     *
     * @param message
     */
    void send(@Param("message") Message message);

    /**
     * 将消息设为已读状态
     *
     * @param msgIds
     */
    void read(@Param("messages") List<String> msgIds);
}
