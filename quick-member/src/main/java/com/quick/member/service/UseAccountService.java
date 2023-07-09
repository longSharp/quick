package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.UseAccountPO;

/**
 * <p>
 * 使用次数账户表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface UseAccountService extends IService<UseAccountPO> {
    /**
     * 根据用户id查询次数账户
     * @param userId
     * @return
     */
    UseAccountPO queryUseAccountByUserId(Long userId);
}
