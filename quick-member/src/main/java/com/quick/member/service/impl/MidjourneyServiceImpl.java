package com.quick.member.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.quick.member.common.config.params.MidjourneyParamsConfig;
import com.quick.member.service.MidjourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private MidjourneyParamsConfig midjourneyParamsConfig;

    @Override
    public void generateImages(String prompt) {
        HttpRequest post = HttpUtil.createPost("");
        Map<String, Object> data = new HashMap<>();
        data.put("type",1);
        data.put("version","1118961510123847772");
        data.put("id","938956540159881230");
        data.put("name","imagine");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("type",3);
        obj.put("name","prompt");
        obj.put("value",prompt);
        list.add(obj);
        data.put("options",list);
        Map<String, Object> body = midjourneyParamsConfig.getBody(2, data, null);
        String bodyStr = JSONUtil.toJsonStr(body);
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        post.body(bodyStr);
        post.headerMap(header,true);
        HttpResponse response = post.execute();
    }
}
