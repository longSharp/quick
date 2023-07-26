package com.quick.gpt.service;


import com.quick.gpt.domain.po.UseAccountPO;
import com.quick.gpt.domain.po.UserMemberPO;
import com.quick.gpt.domain.po.UserProblemLogPO;
import com.quick.gpt.enums.ProblemType;

import java.util.List;

public interface UserProblemLogService {
    /**
     * 获取mongodb的问答记录
     * @param userId userID
     * @param type 问题类型
     * @return
     */
    List<UserProblemLogPO> queryAllByUserId(Long userId, ProblemType type);

    /**
     * 根据类型，用户id和会话id获取mongodb的问答记录
     * @param userId userID
     * @param type 问题类型
     * @return
     */
    List<UserProblemLogPO> queryAllByUserIdAndDiaId(Long userId,Long dialogId, ProblemType type);

    /**
     * 保存问答记录同时更新表
     * @param userMemberPO 会员信息
     * @param useAccountPO 次数账户信息
     * @param answer 答案
     * @param question 问题
     * @param isMember 是否是会员
     * @return
     */
    UserProblemLogPO saveAndUpdate(Long dialogId,
                                   Boolean isUpper,
                                   UserMemberPO userMemberPO,
                                   UseAccountPO useAccountPO,
                                   String answer,
                                   String question,
                                   boolean isMember);
}
