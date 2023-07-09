package com.quick.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.dao.AccountMapper;
import com.quick.member.domain.po.AccountPO;
import com.quick.member.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 余额账户表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountPO> implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public AccountPO queryAccountById(Long id) {
        return accountMapper.selectById(id);
    }

    @Override
    public void addAccount(AccountPO account) {
        accountMapper.insert(account);
    }
}
