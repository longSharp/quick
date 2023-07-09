package com.quick.member.controller;

import com.quick.member.common.enums.ResultCode;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.po.AccountPO;
import com.quick.member.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 余额账户表 前端控制器
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@RestController
@RequestMapping(value="/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 新增数据
     *
     * @param account 实体
     * @return 新增结果
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public R<AccountPO> add(@RequestBody AccountPO account) {
         accountService.addAccount(account);
        return R.ok(ResultCode.REQUEST_SUCCESS.getCode(),ResultCode.REQUEST_SUCCESS.getMsg());
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public R<AccountPO> queryAccountById(@PathVariable Long id){
        AccountPO account = accountService.queryAccountById(id);
        return R.ok("1110","成功",account);
    }
}
