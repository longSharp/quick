package com.quick.gpt.service;


import com.quick.gpt.domain.po.UserDialoguePO;

import java.util.List;

public interface UserDialogueService {
    /**
     * 根据用户id查询该用户下所有会话
     * @param userId
     * @return
     */
    List<UserDialoguePO> getDialogueByUserId(Long userId, Boolean isTopic);

    /**
     * 根据用户id和会话名称查询该用户下所有会话
     * @param userId
     * @return
     */
    List<UserDialoguePO> getDialogueByName(Long userId,String name,Boolean isTopic);

    /**
     * 根据会话id修改会话信息
     * @return
     */
    UserDialoguePO modifyDialogue(UserDialoguePO dialoguePO);

    /**
     * 根据id删除会话
     */
    void deleteDialogueById(Long id);

    /**
     * @return
     */
    UserDialoguePO addDialogue(UserDialoguePO dialoguePO);
}
