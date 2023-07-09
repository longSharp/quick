package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.UseLogPO;

/**
 * <p>
 * 次数使用记录表 服务类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
public interface UseLogService extends IService<UseLogPO> {
    /**
     * 根据用户id检查用户今日是否已签到
     * @param userId
     * @return
     */
    void checkAttendence(Long userId);

    /**
     * 签到接口
     * @param userId
     * @return
     */
    UseLogPO attendance(Long userId);
}
