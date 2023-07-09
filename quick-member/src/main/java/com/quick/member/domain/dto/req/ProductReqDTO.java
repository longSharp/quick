package com.quick.member.domain.dto.req;

import com.quick.member.common.enums.ProductType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductReqDTO {
    @ApiModelProperty("周卡、月卡、季卡、年卡、永久、次卡")
    @NotNull
    private ProductType type;

    @NotBlank
    private String name;

    @ApiModelProperty("可使用次数")
    @NotNull
    private Long useCount;

    @ApiModelProperty("原价")
    private BigDecimal originalPrice;

    @ApiModelProperty("售价")
    @NotNull
    private BigDecimal salePrice;
}
