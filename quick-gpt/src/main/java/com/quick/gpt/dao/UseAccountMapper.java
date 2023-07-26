package com.quick.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.gpt.domain.po.UseAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 使用次数账户表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface UseAccountMapper extends BaseMapper<UseAccountPO> {

}
