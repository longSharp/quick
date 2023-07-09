package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.AccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 余额账户表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountPO> {

}
