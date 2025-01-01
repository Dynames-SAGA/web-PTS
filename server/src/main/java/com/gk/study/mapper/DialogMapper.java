package com.gk.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.study.entity.Dialog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DialogMapper extends BaseMapper<Dialog> {
    List<Dialog> getUserDialogs(String userId);  // 获取用户参与的所有对话

    void deleteMessagesByDialogId(Long dialogId);  // 根据对话ID删除所有消息

    @Select("SELECT * FROM b_Dialog WHERE user1_id = #{userId1} AND user2_id = #{userId2}")
    Dialog findByUserId1AndUserId2(Long userId1, Long userId2);

}
