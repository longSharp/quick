package com.quick.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.gpt.domain.po.UseCountPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UseCountMapper extends BaseMapper<UseCountPO> {
}
