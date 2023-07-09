package com.quick.member.controller;

import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.system.EnumProperties;
import com.quick.member.common.enums.IBaseEnum;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.domain.dto.req.EnumCommonVO;
import com.quick.member.domain.dto.resp.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
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
    public R<List<EnumCommonVO>> getEnum(@PathVariable("enumFullName") String enumFullName) {
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
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
        return R.ok(enumCommonVoS);
    }
}
