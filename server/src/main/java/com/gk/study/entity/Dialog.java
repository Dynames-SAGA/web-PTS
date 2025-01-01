package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("b_dialog")
public class Dialog implements Serializable {
    @TableId(value = "dialog_id", type = IdType.AUTO)
    private Long dialogId; // 对话ID

    @TableField
    private Long user1Id; // 用户1的ID

    @TableField
    private Long user2Id; // 用户2的ID

    @TableField
    private String lastMessageTime; // 最后一条消息的时间

    @TableField
    private String lastMessage; // 最后一条消息的内容

    @TableField(exist = false)
    private String username1; // 用户1的用户名

    @TableField(exist = false)
    private String username2; // 用户2的用户名

    @TableField(exist = false)
    private String avatar1; // 用户1的头像

    @TableField(exist = false)
    private String avatar2; // 用户2的头像
}
