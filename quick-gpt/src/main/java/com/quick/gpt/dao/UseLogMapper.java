package com.quick.gpt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.gpt.domain.po.UseLogPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 次数使用记录表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface UseLogMapper extends BaseMapper<UseLogPO> {

}
