package com.quick.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.member.domain.po.UserAttendancePO;

public interface UserAttendanceService extends IService<UserAttendancePO> {
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
    UserAttendancePO attendance(Long userId);
}
