package com.quick.draw.common.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class JdaUtil {

    public static JDA createJda(String token) throws Exception {
        return JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
    }
}
