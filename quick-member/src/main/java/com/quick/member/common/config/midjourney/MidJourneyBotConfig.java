package com.quick.member.common.config.midjourney;

import com.quick.member.common.config.params.MidjourneyParamsConfig;
import com.quick.member.common.utils.JdaUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidJourneyBotConfig {

    @Autowired
    private MidjourneyParamsConfig config;

    @Autowired
    private MessageListener messageListener;

    @Bean
    public JDA jda() throws Exception {
        JDA jda = JdaUtil.createJda(config.getBotId());
        jda.addEventListener(messageListener);
        return jda;
    }
}
