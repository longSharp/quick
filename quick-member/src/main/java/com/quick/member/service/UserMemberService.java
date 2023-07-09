package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.UserMemberPO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface UserMemberService extends IService<UserMemberPO> {
    /**
     * 根据用户id查询会员
     * @param userId
     * @return
     */
    UserMemberPO queryMemberByUserId(Long userId);
}
