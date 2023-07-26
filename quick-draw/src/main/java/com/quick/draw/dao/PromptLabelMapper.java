package com.quick.draw.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.draw.domain.po.PromptLabelPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromptLabelMapper extends BaseMapper<PromptLabelPO> {
}
