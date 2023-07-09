package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.CardResourcePO;

/**
 * <p>
 * 卡资源表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface CardResourceService extends IService<CardResourcePO> {
    /**
     * 卡密兑换
     * @param userId 用户id
     * @param password 卡密
     */
    void cardResourceConvert(Long userId,String password);
}
