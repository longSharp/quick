package com.quick.member.controller;

import cn.hutool.core.bean.BeanUtil;
import com.quick.member.domain.dto.req.ProductReqDTO;
import com.quick.member.domain.dto.resp.R;
import com.quick.member.domain.po.ProductPO;
import com.quick.member.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/product")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public R<String> addProduct(@Valid @NotNull @RequestBody ProductReqDTO dto){
        ProductPO productPO = BeanUtil.copyProperties(dto, ProductPO.class);
        productService.save(productPO);
        return R.ok(productPO);
    }

}
