package com.gk.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.study.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {


    List<Message> getMessagesByDialog(Long dialogId);  // 根据对话ID查询所有消息

    void deleteMessagesByDialogId(Long dialogId);  // 根据对话ID删除所有消息

}
