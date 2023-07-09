package com.quick.member.domain.dto.req;

import lombok.Data;

/**
 * @Author: Longcm
 * @Description: 获取枚举数据vo
 */
@Data
public class EnumCommonVO {
    /**
     * 枚举名称
     */
    private String code;
    /**
     * 枚举自定义的名称
     */
    private String name;
    /**
     * 枚举索引
     */
    private Integer index;
}
