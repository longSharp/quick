package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.enums.Status;
import com.quick.member.dao.UserMemberMapper;
import com.quick.member.domain.po.UserMemberPO;
import com.quick.member.service.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class UserMemberServiceImpl extends ServiceImpl<UserMemberMapper, UserMemberPO> implements UserMemberService {

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Override
    public UserMemberPO queryMemberByUserId(Long userId) {
        LambdaQueryWrapper<UserMemberPO> query = new LambdaQueryWrapper<>();
        query.eq(UserMemberPO::getUserId,userId)
                .lt(UserMemberPO::getEndDate, LocalDateTime.now())
                .eq(UserMemberPO::getStatus, Status.VALID);
        List<UserMemberPO> userMemberPOS = userMemberMapper.selectList(query);
        if(userMemberPOS.size()<1){
            return null;
        }
        return userMemberPOS.get(0);
    }
}
