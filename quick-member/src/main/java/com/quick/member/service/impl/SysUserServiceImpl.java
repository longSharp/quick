package com.quick.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.enums.*;
import com.quick.member.dao.*;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.domain.dto.resp.UserMemberDTO;
import com.quick.member.domain.po.*;
import com.quick.member.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Autowired
    private UseLogMapper useLogMapper;

    @Autowired
    private BalanceLogMapper balanceLogMapper;

    @Autowired
    private ServiceParamsConfig serviceParamsConfig;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Override
    public SysUserInfoRespDTO queryUserByPhone(String phone) {
        SysUserInfoRespDTO sysUserInfoRespDTO = new SysUserInfoRespDTO();
        QueryWrapper<SysUserPO> sysUserPOQueryWrapper = new QueryWrapper<>();
        sysUserPOQueryWrapper.eq("tel",phone);
        sysUserPOQueryWrapper.eq("status",Status.VALID);
        SysUserPO sysUserPO = userMapper.selectOne(sysUserPOQueryWrapper);

        if(sysUserPO==null) return null;
        BeanUtil.copyProperties(sysUserPO, sysUserInfoRespDTO);
        QueryWrapper<AccountPO> accountPOQueryWrapper = new QueryWrapper<>();
        accountPOQueryWrapper.eq("user_id",sysUserPO.getId())
                        .eq("status",Status.VALID);
        AccountPO account = accountMapper.selectOne(accountPOQueryWrapper);

        QueryWrapper<UseAccountPO> useAccountPOQueryWrapper = new QueryWrapper<>();
        useAccountPOQueryWrapper.eq("user_id",sysUserPO.getId())
                .eq("status",Status.VALID);
        UseAccountPO useAccount = useAccountMapper.selectOne(useAccountPOQueryWrapper);

        if(sysUserPO.getIsMember()==MemberMark.MEMBER){
            QueryWrapper<UserMemberPO> mamberWrapper = new QueryWrapper<>();
            mamberWrapper.eq("user_id",sysUserPO.getId())
                    .eq("status",Status.VALID);
            UserMemberPO member = userMemberMapper.selectOne(mamberWrapper);
            UserMemberDTO userMemberDTO = new UserMemberDTO();
            userMemberDTO.setType(member.getType().getName());
            userMemberDTO.setEndDate(member.getEndDate());
            userMemberDTO.setStartDate(member.getStartDate());
            userMemberDTO.setToDayCount(member.getTodayBalanceCount());
            sysUserInfoRespDTO.setMemberInfo(userMemberDTO);
        }

        if(account!=null){
            sysUserInfoRespDTO.setAccountId(account.getId())
                    .setAccountBalance(account.getMoney());
        }
        if(useAccount!=null){
            sysUserInfoRespDTO.setUseAccountId(useAccount.getId())
                    .setUseCountBalance(useAccount.getBalanceCount());
        }
        return sysUserInfoRespDTO;
    }

    @Override
    public SysUserInfoRespDTO queryUserById(Long id) {
        SysUserInfoRespDTO sysUserInfoRespDTO = new SysUserInfoRespDTO();
        QueryWrapper<SysUserPO> sysUserPOQueryWrapper = new QueryWrapper<>();
        sysUserPOQueryWrapper.eq("id",id);
        sysUserPOQueryWrapper.eq("status",Status.VALID);
        SysUserPO sysUserPO = userMapper.selectOne(sysUserPOQueryWrapper);
        if(sysUserPO==null) throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        BeanUtil.copyProperties(sysUserPO, sysUserInfoRespDTO);
        QueryWrapper<AccountPO> accountPOQueryWrapper = new QueryWrapper<>();
        accountPOQueryWrapper.eq("user_id",sysUserPO.getId())
                .eq("status",Status.VALID);
        AccountPO account = accountMapper.selectOne(accountPOQueryWrapper);

        QueryWrapper<UseAccountPO> useAccountPOQueryWrapper = new QueryWrapper<>();
        useAccountPOQueryWrapper.eq("user_id",sysUserPO.getId())
                .eq("status",Status.VALID);
        UseAccountPO useAccount = useAccountMapper.selectOne(useAccountPOQueryWrapper);

        if(sysUserPO.getIsMember()==MemberMark.MEMBER){
            QueryWrapper<UserMemberPO> mamberWrapper = new QueryWrapper<>();
            mamberWrapper.eq("user_id",sysUserPO.getId())
                    .eq("status",Status.VALID);
            UserMemberPO member = userMemberMapper.selectOne(mamberWrapper);
            UserMemberDTO userMemberDTO = new UserMemberDTO();
            userMemberDTO.setType(member.getType().getName());
            userMemberDTO.setEndDate(member.getEndDate());
            userMemberDTO.setStartDate(member.getStartDate());
            userMemberDTO.setToDayCount(member.getTodayBalanceCount());
            sysUserInfoRespDTO.setMemberInfo(userMemberDTO);
        }

        if(account!=null){
            sysUserInfoRespDTO.setAccountId(account.getId())
                    .setAccountBalance(account.getMoney());
        }
        if(useAccount!=null){
            sysUserInfoRespDTO.setUseAccountId(useAccount.getId())
                    .setUseCountBalance(useAccount.getBalanceCount());
        }
        return sysUserInfoRespDTO;
    }

    @Override
    public SysUserPO queryUserByInviCode(String code) {
        QueryWrapper<SysUserPO> sysUserPOQueryWrapper = new QueryWrapper<>();
        sysUserPOQueryWrapper.eq("invite_code",code);
        sysUserPOQueryWrapper.eq("status",Status.VALID);
        return userMapper.selectOne(sysUserPOQueryWrapper);
    }

    @Transactional
    @Override
    public SysUserInfoRespDTO createUser(String phone, String invitCode) {
        SysUserInfoRespDTO sysUserInfoRespDTO = new SysUserInfoRespDTO();
        //注册者需要创建一系列账号
        //1.创建用户账号
        SysUserPO sysUserPO = new SysUserPO();
        //生成邀请码
        String code = RandomUtil.randomString(serviceParamsConfig.getInvitLength()).toUpperCase(Locale.ROOT);
        sysUserPO.setTel(phone)
                .setInvitePersonCode(invitCode)
                .setInviteCode(code)
                .setIsMember(MemberMark.UNMEMBER)
                .setName(phone)
                .setUserStatus(UserStatus.NORMAL)
                .setRole(UserRole.USERS);
        userMapper.insert(sysUserPO);

        //2.创建余额账户
        AccountPO accountPO = new AccountPO();
        accountPO.setUserId(sysUserPO.getId())
                 .setMoney(serviceParamsConfig.getInitFee());
        accountMapper.insert(accountPO);

        //3.创建次数账户
        UseAccountPO useAccountPO = new UseAccountPO();
        useAccountPO.setUserId(sysUserPO.getId())
                        .setBalanceCount(serviceParamsConfig.getInitUseCount());
        useAccountMapper.insert(useAccountPO);

        //4.生成次数使用记录
        UseLogPO useLogPO = new UseLogPO();
        useLogPO.setType(BalanceType.BRANCH_IN)
                .setEvent(UseAccountEvent.REGISTER)
                .setUseAccountId(useAccountPO.getId())
                .setCount(serviceParamsConfig.getInitUseCount());
        useLogMapper.insert(useLogPO);

        //5.生成余额流水记录
        BalanceLogPO balanceLogPO = new BalanceLogPO();
        balanceLogPO.setType(BalanceType.BRANCH_IN)
                .setEvent(BalanceEvent.REGISTER)
                .setAccountId(accountPO.getId())
                .setMoney(serviceParamsConfig.getInitFee());
        balanceLogMapper.insert(balanceLogPO);

        //推荐者需要处理邀请奖励
        if(!StringUtils.isEmpty(invitCode)){
            //0.先查询推荐者信息
            SysUserPO user = this.queryUserByInviCode(invitCode);
            QueryWrapper<AccountPO> accountQueryWrapper = new QueryWrapper<>();
            accountQueryWrapper.eq("user_id",user.getId());
            accountQueryWrapper.eq("status",Status.VALID);
            AccountPO account = accountMapper.selectOne(accountQueryWrapper);
            QueryWrapper<UseAccountPO> useAccountQueryWrapper = new QueryWrapper<>();
            useAccountQueryWrapper.eq("user_id",user.getId());
            useAccountQueryWrapper.eq("status",Status.VALID);
            UseAccountPO useAccount = useAccountMapper.selectOne(useAccountQueryWrapper);

            //1.奖励余额,生成流水
            account.setMoney(account.getMoney().add(serviceParamsConfig.getInvitFee()));
            accountMapper.updateById(account);
            //生成流水
            BalanceLogPO balanceLog = new BalanceLogPO();
            balanceLog.setType(BalanceType.BRANCH_IN)
                    .setEvent(BalanceEvent.INVITE)
                    .setAccountId(account.getId())
                    .setMoney(serviceParamsConfig.getInvitFee());
            balanceLogMapper.insert(balanceLog);

            //2.奖励次数,生成记录
            useAccount.setBalanceCount(useAccount.getBalanceCount()+serviceParamsConfig.getInvitUseCount());
            useAccountMapper.updateById(useAccount);
            //生成记录
            UseLogPO useLog = new UseLogPO();
            useLog.setType(BalanceType.BRANCH_IN)
                    .setEvent(UseAccountEvent.INVITE)
                    .setUseAccountId(useAccount.getId())
                    .setCount(serviceParamsConfig.getInvitUseCount());
            useLogMapper.insert(useLog);
        }
        BeanUtil.copyProperties(sysUserPO, sysUserInfoRespDTO);
        sysUserInfoRespDTO.setAccountId(accountPO.getId())
                .setAccountBalance(accountPO.getMoney())
                .setUseAccountId(useAccountPO.getId())
                .setUseCountBalance(useAccountPO.getBalanceCount());
        return sysUserInfoRespDTO;

    }

    @Override
    public SysUserInfoRespDTO checkPwd(String phone, String pwd) {
        QueryWrapper<SysUserPO> userWrap = new QueryWrapper<>();
        userWrap.eq("tel",phone);
        userWrap.eq("password",pwd);
        userWrap.eq("status",Status.VALID);
        SysUserPO sysUserPO = userMapper.selectOne(userWrap);
        if(sysUserPO==null){
            return null;
        }
        return this.queryUserByPhone(phone);
    }

    @Override
    public SysUserPO modifyPwd(String phone, String pwd) {
        UpdateWrapper<SysUserPO> sysUserPOUpdateWrapper = new UpdateWrapper<>();
        sysUserPOUpdateWrapper.eq("tel",phone);
        sysUserPOUpdateWrapper.eq("status",Status.VALID);
        SysUserPO sysUserPO = new SysUserPO();
        sysUserPO.setPassword(pwd);
        int update = userMapper.update(sysUserPO,sysUserPOUpdateWrapper);
        if(update>0){
            return sysUserPO;
        }
        return null;
    }
}
