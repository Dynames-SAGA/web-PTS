package com.gk.study.service;

import com.gk.study.entity.Dialog;
import com.gk.study.entity.Message;
import java.util.List;

public interface DialogService {

    /**
     * 根据用户ID获取用户的所有对话
     * @param userId 用户ID
     * @return 对话列表
     */
    List<Dialog> getUserDialogs(String userId);

    /**
     * 创建一个新的对话
     * @param userId1 用户ID1
     * @param userId2 用户ID2
     * @return 创建的对话对象
     */
    Dialog createDialog(Long userId1, Long userId2);

    /**
     * 根据对话ID删除对话及其所有消息
     * @param dialogId 对话ID
     */
    void deleteDialog(Long dialogId);

    /**
     * 根据对话ID获取所有消息
     * @param dialogId 对话ID
     * @return 消息列表
     */
    List<Message> getMessages(Long dialogId);

    /**
     * 发送消息到指定对话
     * @param dialogId 对话ID
     * @param senderId 发送者ID
     * @param content 消息内容
     * @return 创建的消息对象
     */
    Message sendMessage(Long dialogId, Long senderId, String content);

    /**
     * 根据消息ID删除消息
     * @param messageId 消息ID
     */
    void deleteMessage(Long messageId);
}
