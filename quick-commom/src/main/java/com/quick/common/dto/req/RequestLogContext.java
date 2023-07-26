package com.quick.common.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Data
@Accessors(chain = true)
public class RequestLogContext {
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 请求方法
     */
    private String requestMethod;
    /**
     * 请求query参数
     */
    private String requestQuery;
    /**
     * 请求body参数
     */
    private String requestBody;
    /**
     * sessionId
     */
    private String requestSessionId;
    /**
     * 请求方的ip地址
     */
    private String remoteAddr;
    /**
     * 请求返回的数据
     */
    private String responseData;

    /**
     * 请求时长
     */
    private String requestDuration;

    public String toString() {
        if(StringUtils.isEmpty(requestQuery)&&StringUtils.isEmpty(requestBody)){
            return "\n###############start##############\n"+
                    "#requestUrl="+requestUrl+"\n"+
                    "#requestMethod="+requestMethod+"\n"+
                    "#requestSessionId="+requestSessionId+"\n"+
                    "#remoteAddr="+remoteAddr+"\n"+
                    "#requestDuration="+requestDuration+"\n"+
                    "################end##############\n";
        }
        if(StringUtils.isEmpty(requestQuery)){
            return "\n###############start##############\n"+
                    "#requestUrl="+requestUrl+"\n"+
                    "#requestMethod="+requestMethod+"\n"+
                    "#requestBody="+requestBody+"\n"+
                    "#requestSessionId="+requestSessionId+"\n"+
                    "#remoteAddr="+remoteAddr+"\n"+
                    "#requestDuration="+requestDuration+"\n"+
                    "################end##############\n";
        }
        if(StringUtils.isEmpty(requestBody)){
            return "\n###############start##############\n"+
                    "#requestUrl="+requestUrl+"\n"+
                    "#requestMethod="+requestMethod+"\n"+
                    "#requestSessionId="+requestSessionId+"\n"+
                    "#requestQuery="+requestQuery+"\n"+
                    "#remoteAddr="+remoteAddr+"\n"+
                    "#requestDuration="+requestDuration+"\n"+
                    "################end##############\n";
        }
        return "\n###############start##############\n"+
                "#requestUrl="+requestUrl+"\n"+
                "#requestMethod="+requestMethod+"\n"+
                "#requestQuery="+requestQuery+"\n"+
                "#requestBody="+requestBody+"\n"+
                "#requestSessionId="+requestSessionId+"\n"+
                "#remoteAddr="+remoteAddr+"\n"+
                "#requestDuration="+requestDuration+"\n"+
                "################end##############\n";
    }
}
