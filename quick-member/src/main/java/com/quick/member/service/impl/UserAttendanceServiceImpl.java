package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.enums.ResultCode;
import com.quick.common.enums.Status;
import com.quick.common.exception.BusinessException;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.enums.BalanceType;
import com.quick.member.common.enums.UseAccountEvent;
import com.quick.member.dao.UseAccountMapper;
import com.quick.member.dao.UseLogMapper;
import com.quick.member.dao.UserAttendanceMapper;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.domain.po.UseLogPO;
import com.quick.member.domain.po.UserAttendancePO;
import com.quick.member.service.UserAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserAttendanceServiceImpl extends ServiceImpl<UserAttendanceMapper, UserAttendancePO> implements UserAttendanceService {

    @Autowired
    private UserAttendanceMapper userAttendanceMapper;

    @Autowired
    private ServiceParamsConfig serviceParamsConfig;

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Autowired
    private UseLogMapper useLogMapper;


    @Override
    public void checkAttendence(Long userId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        LambdaQueryWrapper<UserAttendancePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(UserAttendancePO::getCreateTime, today).eq(UserAttendancePO::getUserId,userId);
        UserAttendancePO userAttendancePO = userAttendanceMapper.selectOne(queryWrapper);
        if(userAttendancePO!=null){
            throw new BusinessException(ResultCode.USER_ATTENDANCED.getCode(),ResultCode.USER_ATTENDANCED.getMsg());
        }
    }

    @Override
    public UserAttendancePO attendance(Long userId) {
        UserAttendancePO userAttendancePO = new UserAttendancePO();
        userAttendancePO.setUserId(userId);
        int insert = userAttendanceMapper.insert(userAttendancePO);
        if(insert<1){
            throw new BusinessException(ResultCode.USER_ATTENDANCE_FAIL.getCode(),ResultCode.USER_ATTENDANCE_FAIL.getMsg());
        }
        //签到成功更新用户次数账户以及记录
        //更新次数账户
        QueryWrapper<UseAccountPO> useAccountQueryWrapper = new QueryWrapper<>();
        useAccountQueryWrapper.eq("user_id",userId);
        useAccountQueryWrapper.eq("status", Status.VALID);
        UseAccountPO useAccount = useAccountMapper.selectOne(useAccountQueryWrapper);
        useAccount.setBalanceCount(useAccount.getBalanceCount()+serviceParamsConfig.getAttendanceCount());
        useAccountMapper.updateById(useAccount);
        //生成记录
        UseLogPO useLog = new UseLogPO();
        useLog.setType(BalanceType.BRANCH_IN)
                .setEvent(UseAccountEvent.ATTENDANCE)
                .setUseAccountId(useAccount.getId())
                .setCount(serviceParamsConfig.getAttendanceCount());
        useLogMapper.insert(useLog);
        return userAttendancePO;
    }
}
