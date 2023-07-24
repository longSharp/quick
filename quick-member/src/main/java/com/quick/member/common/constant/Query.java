/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.quick.member.common.constant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 查询参数
 *
 * @author iafoot
 * @email iafoot.han@gmail.com
 * @date 2021-07-21 09:29:05
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Long> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Long> params, String orderField, Boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(Constant.PAGE) != null){
            curPage = params.get(Constant.PAGE);
        }
        if(params.get(Constant.LIMIT) != null){
            limit = params.get(Constant.LIMIT);
        }
        //分页对象
        Page<T> page = new Page<>(curPage, limit);


        //没有排序字段，则不排序
        if(StringUtils.isBlank(orderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(orderField));
        }else {
            page.addOrder(OrderItem.desc(orderField));
        }

        return page;
    }
}
