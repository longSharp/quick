package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

}
