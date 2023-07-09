package com.quick.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.member.domain.po.UserPicturePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户图库表 Mapper 接口
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Mapper
public interface UserPictureMapper extends BaseMapper<UserPicturePO> {

}
