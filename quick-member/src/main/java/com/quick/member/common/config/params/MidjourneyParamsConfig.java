package com.quick.member.common.config.params;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "midjourney")
@Data
public class MidjourneyParamsConfig {
    /**
     * 服务器id
     */
    private String guildId;
    /**
     * 频道id
     */
    private String channelId;
    /**
     * 应用id
     */
    private String applicationId;
    /**
     * sessionid
     */
    private String sessionId;
    /**
     * 登入凭证
     */
    private String token;

    /**
     * 获取body参数
     * @param type 请求类型
     * @param data 请求数据
     * @param other 其他参数
     * @return
     */
    public Map<String,Object> getBody(Integer type,Map<String,Object> data,Map<String,Object> other){
        Map<String, Object> body = new HashMap<>();
        body.put("type",type);
        body.put("application_id",applicationId);
        body.put("guild_id",guildId);
        body.put("channel_id",channelId);
        body.put("session_id",sessionId);
        body.put("data",data);
        if(other!=null){
            Set<Map.Entry<String, Object>> entries = other.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                body.put(entry.getKey(),entry.getValue());
            }
        }
        return body;
    }

    /**
     * 获取头部参数
     * @return
     */
    public Map<String,String> getHeader(){
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("Authorization",token);
        return header;
    }

}
