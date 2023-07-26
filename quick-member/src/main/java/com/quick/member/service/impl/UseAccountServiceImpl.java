package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.enums.Status;
import com.quick.member.dao.UseAccountMapper;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.service.UseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 使用次数账户表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class UseAccountServiceImpl extends ServiceImpl<UseAccountMapper, UseAccountPO> implements UseAccountService {

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Override
    public UseAccountPO queryUseAccountByUserId(Long userId) {
        LambdaQueryWrapper<UseAccountPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UseAccountPO::getStatus, Status.VALID)
                .eq(UseAccountPO::getUserId, userId);
        return useAccountMapper.selectOne(queryWrapper);
    }
}
