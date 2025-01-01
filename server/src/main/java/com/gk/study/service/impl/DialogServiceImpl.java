package com.gk.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.entity.Dialog;
import com.gk.study.entity.Message;
import com.gk.study.entity.User;
import com.gk.study.mapper.DialogMapper;
import com.gk.study.mapper.UserMapper;
import com.gk.study.mapper.MessageMapper;
import com.gk.study.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@Service
public class DialogServiceImpl extends ServiceImpl<DialogMapper, Dialog> implements DialogService {

    @Autowired
    private DialogMapper dialogMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Dialog> getUserDialogs(String userId) {
        List<Dialog> dialogs = dialogMapper.getUserDialogs(userId);
        for (Dialog dialog : dialogs) {
            Long user1id = dialog.getUser1Id();
            Long user2id = dialog.getUser2Id();

            User user1 = userMapper.selectById(user1id);
            dialog.setUsername1(user1.getUsername());
            dialog.setAvatar1(user1.getAvatar());

            User user2 = userMapper.selectById(user2id);
            dialog.setUsername2(user2.getUsername());
            dialog.setAvatar2(user2.getAvatar());
        }

        return dialogs;
    }

    @Override
    public Dialog createDialog(Long userId1, Long userId2) {
        // 创建对话
        Dialog dialog = new Dialog();
        if (userId1 > userId2) {
            Long temp = userId1;
            userId1 = userId2;
            userId2 = temp;
        }

        Dialog existingDialog = dialogMapper.findByUserId1AndUserId2(userId1, userId2);
        if (existingDialog != null) {
            return existingDialog; // 如果对话已经存在，直接返回
        }

        dialog.setUser1Id(userId1);
        dialog.setUser2Id(userId2);
        dialogMapper.insert(dialog); // 插入对话

        // 返回创建的对话
        return dialog;
    }

    @Override
    public void deleteDialog(Long dialogId) {
        // 删除对话的所有消息
        messageMapper.deleteMessagesByDialogId(dialogId);
        // 删除对话
        dialogMapper.deleteById(dialogId);
    }

    @Override
    public List<Message> getMessages(Long dialogId) {
        List<Message> messages = messageMapper.getMessagesByDialog(dialogId);
        for (Message message : messages) {
            Long userid = message.getSenderId();
            User user = userMapper.selectById(userid);
            message.setSenderUsername(user.getUsername());
            message.setSenderAvatar(user.getAvatar());
        }
        return messages;
    }

    @Override
    public Message sendMessage(Long dialogId, Long senderId, String content) {
        // 创建消息
        Message message = new Message();
        message.setDialogId(dialogId);
        message.setSenderId(senderId);
        message.setContent(content);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date(System.currentTimeMillis()));
        message.setTimestamp(formattedDate);

        // 插入消息
        messageMapper.insert(message);

        // 返回发送的消息
        return message;
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageMapper.deleteById(messageId);
    }
}
