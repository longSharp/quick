package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.common.enums.DialogTopicType;
import com.quick.member.domain.po.DialogueTopicPO;

import java.util.List;

public interface DialogueTopicService  extends IService<DialogueTopicPO> {
    /**
     * 根据类型查询主题
     * @param type
     * @return
     */
    List<DialogueTopicPO> getDialogueTopicsByType(Integer type);

    void insert(DialogueTopicPO topic);

}
