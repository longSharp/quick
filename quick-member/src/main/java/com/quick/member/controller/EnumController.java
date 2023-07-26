package com.quick.member.controller;

import com.quick.common.dto.resp.R;
import com.quick.common.enums.IBaseEnum;
import com.quick.common.enums.ResultCode;
import com.quick.common.exception.BusinessException;
import com.quick.member.common.config.system.EnumProperties;
import com.quick.member.domain.dto.req.EnumCommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/types")
@Validated
public class EnumController {
    @Autowired
    private EnumProperties enumProperties;


    /**
     * 枚举值获取
     */
    @RequestMapping(value = "/getEnum/{enumFullName}")
    public R<String> getEnum(@PathVariable("enumFullName") String enumFullName) {
        List<EnumCommonVO> enumCommonVoS = new ArrayList<>();
        try {
            Class c = enumProperties.getEnum(enumFullName.toLowerCase(Locale.ROOT));
            Object[] enumValues = c.getEnumConstants();
            for (Object o : enumValues) {
                EnumCommonVO enumCommonVO = new EnumCommonVO();
                Enum e = (Enum) o;
                String value="";
                if (e instanceof IBaseEnum){
                    value = ((IBaseEnum)o).getName();
                }
                enumCommonVO.setName(value);
                enumCommonVO.setCode(e.name());
                enumCommonVO.setIndex(e.ordinal());
                enumCommonVoS.add(enumCommonVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
        return R.ok(enumCommonVoS);
    }
}
