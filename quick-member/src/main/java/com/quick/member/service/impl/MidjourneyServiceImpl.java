package com.quick.member.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.config.params.MidjourneyParamsConfig;
import com.quick.member.common.constant.MidJourneyUrlConstant;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.ImageJobStatus;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.common.utils.TransApi;
import com.quick.member.dao.ImageJobMapper;
import com.quick.member.domain.dto.resp.BaiduTransRespDTO;
import com.quick.member.domain.dto.resp.BaidutransResult;
import com.quick.member.domain.po.ImageJobPO;
import com.quick.member.service.MidjourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private MidjourneyParamsConfig midjourneyParamsConfig;

    @Autowired
    private ImageJobMapper imageJobMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    public TransApi transApi;

    @Transactional
    @Override
    public void generateImages(String prompt,Long userId) {
        ImageJobPO imageJobPO = new ImageJobPO();
        imageJobPO.setJobStatus(ImageJobStatus.GENERATING)
                .setUserId(userId)
                .setPrompt(prompt)
                .setJobSchedule("0");
        imageJobMapper.insert(imageJobPO);
        Long jobId = imageJobPO.getId();
        redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK+userId,jobId+"",JSONUtil.toJsonStr(imageJobPO));
        redisTemplate.opsForValue().set(RedisKeyPrefixConstant.TASK_USER_ID+jobId,userId+"");
        //中文转英文
        String transResult = transApi.getTransResult(prompt, "auto", "en");
        BaiduTransRespDTO baiduTransRespDTO = JSONUtil.toBean(transResult, BaiduTransRespDTO.class);
        if(baiduTransRespDTO==null||baiduTransRespDTO.getTrans_result()==null||baiduTransRespDTO.getTrans_result().size()<1){
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
        BaidutransResult baidutransResult = baiduTransRespDTO.getTrans_result().get(0);
        String promptTrans = "([Ignore this " +
                jobId +
                "])"+baidutransResult.getDst();
        HttpRequest post = HttpUtil.createPost(MidJourneyUrlConstant.GET_IMAGES);
        Map<String, Object> data = new HashMap<>();
        data.put("type",1);
        data.put("version","1118961510123847772");
        data.put("id","938956540159881230");
        data.put("name","imagine");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("type",3);
        obj.put("name","prompt");
        obj.put("value", promptTrans);
        list.add(obj);
        data.put("options",list);
        Map<String, Object> body = midjourneyParamsConfig.getBody(2, data, null);
        String bodyStr = JSONUtil.toJsonStr(body);
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        post.body(bodyStr);
        post.headerMap(header,true);
        HttpResponse response = post.execute();
        System.out.println("response status:"+response.getStatus());
    }
}
