package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("b_message")
public class Message implements Serializable {
    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId; // 消息ID

    @TableField
    private Long dialogId; // 对话ID

    @TableField
    private Long senderId; // 发送者ID

    @TableField
    private String content; // 消息内容

    @TableField
    private String timestamp; // 消息时间戳


    @TableField(exist = false)
    private String senderUsername; // 发送者的用户名

    @TableField(exist = false)
    private String senderAvatar; // 发送者的头像
}
