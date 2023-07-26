package com.quick.member.controller;

import com.quick.common.dto.resp.R;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.service.UseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 使用次数账户表 前端控制器
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/account")
public class UseAccountController {
    @Autowired
    private UseAccountService useAccountService;

    @GetMapping("queryUseAccountByUserId")
    public R<String> queryUseAccountByUserId(@RequestParam Long userId){
        UseAccountPO useAccountPO = useAccountService.queryUseAccountByUserId(userId);
        return R.ok(useAccountPO);
    }
}
