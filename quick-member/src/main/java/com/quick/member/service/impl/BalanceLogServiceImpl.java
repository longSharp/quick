package com.quick.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.dao.BalanceLogMapper;
import com.quick.member.domain.po.BalanceLogPO;
import com.quick.member.service.BalanceLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 余额流水记录表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class BalanceLogServiceImpl extends ServiceImpl<BalanceLogMapper, BalanceLogPO> implements BalanceLogService {

}
