package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.AccountPO;

/**
 * <p>
 * 余额账户表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface AccountService extends IService<AccountPO> {

    AccountPO queryAccountById(Long id);

    void addAccount(AccountPO account);
}
