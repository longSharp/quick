package com.quick.member.common.config.midjourney;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidJourneyBotConfig {

    private static final String TOKEN = "MTEyMzQzMDMxNjM2Mzc1NTY5MA.Gi3WaT.VQmcwk_6B-K8ahIdvEI-6Aqr2sxH66WZ5Ru8Aw";

//    @Bean
    public JDA jda() {
        JDA jda = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.addEventListener(new MessageListener());
        return jda;
    }
}
