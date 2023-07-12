package com.quick.member.common.config.system;

import com.quick.member.common.config.params.BaiduParamsConfig;
import com.quick.member.common.utils.TransApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SystemBeanConfig {

    @Autowired
    private BaiduParamsConfig baiduParamsConfig;

    @Bean
    public TransApi transApi(){
        return new TransApi(baiduParamsConfig.getTransApiHost(),baiduParamsConfig.getAppId(), baiduParamsConfig.getSecurityKey());
    }
}
