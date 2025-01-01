package com.gk.study.controller;

import com.gk.study.common.APIResponse;
import com.gk.study.common.ResponeCode;
import com.gk.study.entity.Dialog;
import com.gk.study.entity.Message;
import com.gk.study.service.DialogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dialog")
public class DialogController {

    private final static Logger logger = LoggerFactory.getLogger(DialogController.class);

    @Autowired
    private DialogService dialogService;

    // 查看某个用户参与的所有对话
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public APIResponse listUserDialogs(String userId) {
        List<Dialog> list = dialogService.getUserDialogs(userId);
        return new APIResponse(ResponeCode.SUCCESS, "查询成功", list);
    }

    // 创建对话
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Transactional
    public APIResponse createDialog(Long user1Id, Long user2Id) {
        Dialog dialog = dialogService.createDialog(user1Id, user2Id);
        return new APIResponse(ResponeCode.SUCCESS, "对话创建成功", dialog);
    }

    // 查看某个对话的所有消息
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public APIResponse listMessages(Long dialogId) {
        List<Message> messages = dialogService.getMessages(dialogId);
        return new APIResponse(ResponeCode.SUCCESS, "查询成功", messages);
    }

    // 发送消息
    @RequestMapping(value = "/message/send", method = RequestMethod.POST)
    @Transactional
    public APIResponse sendMessage(Long dialogId, Long senderId, String content) {
        Message message = dialogService.sendMessage(dialogId, senderId, content);
        return new APIResponse(ResponeCode.SUCCESS, "消息发送成功", message);
    }

    // 删除消息
    @RequestMapping(value = "/message/delete", method = RequestMethod.POST)
    public APIResponse deleteMessage(Long messageId) {
        dialogService.deleteMessage(messageId);
        return new APIResponse(ResponeCode.SUCCESS, "消息删除成功");
    }

    // 删除对话
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public APIResponse deleteDialog(Long dialogId) {
        dialogService.deleteDialog(dialogId);
        return new APIResponse(ResponeCode.SUCCESS, "对话删除成功");
    }
}
