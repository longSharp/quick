package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.UserMemberPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface UserMemberMapper extends BaseMapper<UserMemberPO> {

}
