package com.quick.draw.common.config.midjourney;

import com.quick.draw.common.config.params.MidjourneyParamsConfig;
import com.quick.draw.common.utils.JdaUtil;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidJourneyBotConfig {

    @Autowired
    private MidjourneyParamsConfig config;

    @Autowired
    private MessageListener messageListener;

//    @Bean
    public JDA jda() throws Exception {
        JDA jda = JdaUtil.createJda(config.getBotId());
        jda.addEventListener(messageListener);
        return jda;
    }
}
