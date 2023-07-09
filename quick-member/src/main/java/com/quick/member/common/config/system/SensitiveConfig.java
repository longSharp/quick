package com.quick.member.common.config.system;

import cn.hutool.dfa.SensitiveUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SensitiveConfig {

    @Bean
    public SensitiveUtil sensitiveUtil() throws IOException {
//        URL path = ResourceUtil.getResource("config/sensitive_words_lines.txt");
//        File file = FileUtil.file(path);
        ClassPathResource resource = new ClassPathResource("config/sensitive_words_lines.txt");
        List<String> list = new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream(), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null){
            list.add(line);
        }
        bufferedReader.close();
        SensitiveUtil.init(list,true);
        return new SensitiveUtil();
    }
}
