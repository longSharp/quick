package com.quick.member.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.models.RuntimeOptions;
import com.quick.common.enums.ResultCode;
import com.quick.member.common.config.params.AuthSmsParamsConfig;
import com.quick.member.common.utils.HttpUtils;
import com.quick.member.domain.dto.req.AliSmsParamsDTO;
import com.quick.member.domain.dto.resp.AuthSmsBaseResp;
import com.quick.member.service.IAuthSmsService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Longcm
 * @Description:
 */
@Service
public class AuthSmsServiceImpl implements IAuthSmsService {

    @Autowired
    private AuthSmsParamsConfig authSmsParamsConfig;

    @Override
    public AuthSmsBaseResp sendCode(String phone, String code) {
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + authSmsParamsConfig.getAppCode());
        Map<String, String> querys = new HashMap<>(16);
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");
        querys.put("smsSignId", authSmsParamsConfig.getSmsSignId());
        querys.put("templateId", authSmsParamsConfig.getTemplateId());
        Map<String, String> bodys = new HashMap<>(16);
        try {
            HttpResponse response = HttpUtils.doPost(authSmsParamsConfig.getHost(), authSmsParamsConfig.getPath(), method, headers, querys, bodys);
            //获取response的body
            String body = EntityUtils.toString(response.getEntity(),"UTF-8");
            JSONObject bodyJson = JSONUtil.parseObj(body);
            String respCode = String.valueOf(bodyJson.get("code"));
            String respMsg = String.valueOf(bodyJson.get("msg"));
            return AuthSmsBaseResp.builder().code(respCode).msg(respMsg).build();
        } catch (Exception e) {
            e.printStackTrace();
            return AuthSmsBaseResp
                    .builder()
                    .code(ResultCode.SMS_CODE_FAIL.getCode())
                    .msg(ResultCode.SMS_CODE_FAIL.getMsg())
                    .build();
        }
    }

    @Override
    public AuthSmsBaseResp sendCodeNew(String phone, String code) throws Exception {
        Client client = createClientWithSTS(authSmsParamsConfig.getAccessKeyId(), authSmsParamsConfig.getAccessKeySecret(), authSmsParamsConfig.getSecurityToken());
        Params params = createApiInfo();
        RuntimeOptions runtime = new RuntimeOptions();
        OpenApiRequest request = new OpenApiRequest();
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("PhoneNumbers",phone);
        queryParams.put("SignName",authSmsParamsConfig.getSmsSignId());
        queryParams.put("TemplateCode",authSmsParamsConfig.getTemplateId());
        queryParams.put("TemplateParam","{\"code\":"+code+"}");
        request.setQuery(queryParams);

        AliSmsParamsDTO bodyParams = new AliSmsParamsDTO();
        bodyParams.setPhoneNumbers(phone)
                .setSignName(authSmsParamsConfig.getSmsSignId())
                .setTemplateCode(authSmsParamsConfig.getTemplateId())
                .setTemplateParam("{\"code\":"+code+"}");
        request.setBody(bodyParams);
        Map<String, ?> resultMap = client.callApi(params, request, runtime);
        Object body = resultMap.get("body");
        JSONObject jsonObj = JSONUtil.parseObj(body);
        Object respCode = jsonObj.get("Code");
        if("OK".equals(respCode)){
            return AuthSmsBaseResp.builder().code(ResultCode.REQUEST_SUCCESS.getCode()).msg(ResultCode.REQUEST_SUCCESS.getMsg()).build();
        }
        return AuthSmsBaseResp.builder().code(ResultCode.SMS_CODE_FAIL.getCode()).msg(ResultCode.SMS_CODE_FAIL.getMsg()).build();
    }

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    public Client createClientWithSTS(String accessKeyId, String accessKeySecret, String securityToken) throws Exception {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret)
                // 必填，您的 Security Token
                .setSecurityToken(securityToken)
                // 必填，表明使用 STS 方式
                .setType("sts");
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    public static Params createApiInfo(){
        Params params = new Params()
                // 接口名称
                .setAction("SendSms")
                // 接口版本
                .setVersion("2017-05-25")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("RPC")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("json")
                // 接口响应体内容格式
                .setBodyType("json");
        return params;
    }


}
