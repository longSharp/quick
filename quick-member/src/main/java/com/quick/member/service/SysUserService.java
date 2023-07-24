package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.common.enums.UserRole;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.domain.po.SysUserPO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface SysUserService extends IService<SysUserPO> {
    /**
     * 根据电话号码查用户
     * @param phone 电话号码
     * @return 返回用户对象，没有则返回null
     */
    SysUserInfoRespDTO queryUserByPhone(String phone);

    /**
     * 根据ip和用户角色用户
     */
    SysUserInfoRespDTO queryUserByIp(UserRole role,String ip);

    /**
     * 根据用户id查用户
     * @param id 用户id
     * @return 返回用户对象，没有则返回null
     */
    SysUserInfoRespDTO queryUserById(Long id);

    /**
     * 根据邀请码查用户
     * @param code 邀请码
     * @return 返回用户对象，没有则返回null
     */
    SysUserPO queryUserByInviCode(String code);

    /**
     * 创建用户
     * @param phone 用户电话号码
     */
    SysUserInfoRespDTO createUser(String phone, String invitCode);

    /**
     * 用户密码校验登入
     * @param phone 手机号码
     * @param pwd 密码
     * @return 返回用户对象，没有则返回null
     */
    SysUserInfoRespDTO checkPwd(String phone, String pwd);

    /**
     * 创建游客用户
     */
    SysUserInfoRespDTO touristRegister(String ip);

    /**
     * 用户密码修改
     * @param phone 手机号码
     * @param pwd 密码
     * @return 返回用户对象，没有则返回null
     */
    SysUserPO modifyPwd(String phone,String pwd);
}
