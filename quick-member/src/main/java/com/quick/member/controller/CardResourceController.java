package com.quick.member.controller;

import cn.hutool.core.util.RandomUtil;
import com.quick.common.dto.resp.R;
import com.quick.common.enums.ResultCode;
import com.quick.common.exception.BusinessException;
import com.quick.member.common.enums.UserRole;
import com.quick.member.common.enums.UserStatus;
import com.quick.member.common.utils.UserHolder;
import com.quick.member.domain.dto.resp.SysUserInfoRespDTO;
import com.quick.member.domain.po.CardResourcePO;
import com.quick.member.service.CardResourceService;
import com.quick.member.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/cardResource")
public class CardResourceController {

    @Autowired
    private CardResourceService cardResourceService;

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/addCard")
    public R<String> addCard(@Valid @NotNull @RequestParam Long productId){
        String password = RandomUtil.randomString(32);
        CardResourcePO po = new CardResourcePO();
        po.setCardPassword(password);
        cardResourceService.save(po);
        return R.ok(po);
    }

    @PostMapping("/cardResourceConvert")
    public R cardResourceConvert(@Valid @NotBlank @RequestParam String cardPwd){
        Long userId = UserHolder.getUserId();
        checkUser(userId);
        cardResourceService.cardResourceConvert(userId,cardPwd);
        return R.ok();
    }

    private void checkUser(Long userId){
        SysUserInfoRespDTO sysUserInfoRespDTO = sysUserService.queryUserById(userId);
        if(sysUserInfoRespDTO==null){
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }
        UserStatus userStatus = sysUserInfoRespDTO.getUserStatus();
        if(!userStatus.getCode().equals(UserStatus.NORMAL.getCode())){
            throw new BusinessException(ResultCode.USE_STATUS_ERROR);
        }
        UserRole role = sysUserInfoRespDTO.getRole();
        if(role.getCode().equals(UserRole.TOURIST.getCode())){
            throw new BusinessException(ResultCode.SESSION_EMPTY);
        }
    }




}
