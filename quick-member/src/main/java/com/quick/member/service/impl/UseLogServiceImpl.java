package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.enums.BalanceType;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.common.enums.Status;
import com.quick.member.common.enums.UseAccountEvent;
import com.quick.member.dao.UseAccountMapper;
import com.quick.member.dao.UseLogMapper;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.domain.po.UseLogPO;
import com.quick.member.domain.po.UserAttendancePO;
import com.quick.member.service.UseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 次数使用记录表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class UseLogServiceImpl extends ServiceImpl<UseLogMapper, UseLogPO> implements UseLogService {

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Autowired
    private UseLogMapper useLogMapper;

    @Autowired
    private ServiceParamsConfig serviceParamsConfig;

    @Override
    public void checkAttendence(Long userId) {
        QueryWrapper<UseAccountPO> useAccountQueryWrapper = new QueryWrapper<>();
        useAccountQueryWrapper.eq("user_id",userId);
        useAccountQueryWrapper.eq("status", Status.VALID);
        UseAccountPO useAccount = useAccountMapper.selectOne(useAccountQueryWrapper);
        if(useAccount==null){
            throw new BusinessException(ResultCode.USER_ATTENDANCE_FAIL.getCode(),ResultCode.USER_ATTENDANCE_FAIL.getMsg());
        }
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        LambdaQueryWrapper<UseLogPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(UseLogPO::getCreateTime, today)
                .eq(UseLogPO::getUseAccountId,useAccount.getId())
                .eq(UseLogPO::getStatus,Status.VALID)
                .eq(UseLogPO::getEvent,UseAccountEvent.ATTENDANCE);
        List<UseLogPO> useLog = useLogMapper.selectList(queryWrapper);
        if(useLog!=null&&useLog.size()>0){
            throw new BusinessException(ResultCode.USER_ATTENDANCED.getCode(),ResultCode.USER_ATTENDANCED.getMsg());
        }
    }

    @Transactional
    @Override
    public UseLogPO attendance(Long userId) {
        //更新次数账户
        QueryWrapper<UseAccountPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("status", Status.VALID);
        UseAccountPO useAccount = useAccountMapper.selectOne(queryWrapper);

        useAccount.setBalanceCount(useAccount.getBalanceCount()+serviceParamsConfig.getAttendanceCount());
        useAccountMapper.updateById(useAccount);
        //生成记录
        UseLogPO useLog = new UseLogPO();
        useLog.setType(BalanceType.BRANCH_IN)
                .setEvent(UseAccountEvent.ATTENDANCE)
                .setUseAccountId(useAccount.getId())
                .setCount(serviceParamsConfig.getAttendanceCount());
        useLogMapper.insert(useLog);
        return useLog;
    }
}
