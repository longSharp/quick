package com.quick.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.dao.UserPictureMapper;
import com.quick.member.domain.po.UserPicturePO;
import com.quick.member.service.UserPictureService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户图库表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class UserPictureServiceImpl extends ServiceImpl<UserPictureMapper, UserPicturePO> implements UserPictureService {

}
