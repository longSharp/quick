package com.quick.member.controller;

import com.quick.common.dto.resp.R;
import com.quick.member.domain.po.UserMemberPO;
import com.quick.member.service.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@RestController
@RequestMapping("member")
public class UserMemberController {
    @Autowired
    private UserMemberService userMemberService;

    @GetMapping("queryMemberByUserId")
    public R<String> queryMemberByUserId(@RequestParam Long userId) {
        UserMemberPO userMemberPO = userMemberService.queryMemberByUserId(userId);
        return R.ok(userMemberPO);
    }

}
