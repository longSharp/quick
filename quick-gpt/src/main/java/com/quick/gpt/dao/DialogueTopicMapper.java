package com.quick.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.gpt.domain.po.DialogueTopicPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DialogueTopicMapper extends BaseMapper<DialogueTopicPO> {
}
