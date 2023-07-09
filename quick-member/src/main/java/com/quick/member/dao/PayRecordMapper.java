package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.PayRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 支付记录表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface PayRecordMapper extends BaseMapper<PayRecordPO> {

}
