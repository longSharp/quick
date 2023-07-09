package com.quick.member.domain.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

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
        return "\n###############start##############\n"+
                "#requestUrl="+requestUrl+"\n"+
                "#requestMethod="+requestMethod+"\n"+
                "#requestQuery="+requestQuery+"\n"+
                "#requestBody="+requestBody+"\n"+
                "#requestSessionId="+requestSessionId+"\n"+
                "#remoteAddr="+remoteAddr+"\n"+
                "#responseData="+responseData+"\n"+
                "#requestDuration="+requestDuration+"\n"+
                "################end##############\n";
    }
}
