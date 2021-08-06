package com.cqut.ktp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 欧阳建荣
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    /**
     * 消息编号
     */
    private String msgId;

    /**
     * 发送方,uid
     */
    private User from;

    /**
     * 接收方,uid
     */
    private User to;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息状态，1->已读，2->未读
     */
    private Integer status;
}
