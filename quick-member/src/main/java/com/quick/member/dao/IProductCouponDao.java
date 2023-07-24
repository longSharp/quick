package com.quick.member.dao;

import com.quick.member.domain.po.ProductCouponPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductCouponDao extends PageBaseMapper<ProductCouponPO> {
}
