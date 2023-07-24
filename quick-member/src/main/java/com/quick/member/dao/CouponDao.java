package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.CouponPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponDao extends BaseMapper<CouponPO> {
}
