package com.quick.member.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.ImageJobStatus;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.dao.ImageJobMapper;
import com.quick.member.domain.po.ImageJobPO;
import com.quick.member.service.ImagerJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImagerJobServiceImpl implements ImagerJobService {

    @Autowired
    private ImageJobMapper imageJobMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ImageJobPO> getJobByUserId(Long userId,Integer taskStatus) {
        if(Objects.equals(ImageJobStatus.CREATED.getCode(), taskStatus)
                || Objects.equals(ImageJobStatus.CREATING.getCode(), taskStatus)
                || Objects.equals(ImageJobStatus.GENERATING.getCode(), taskStatus)){
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisKeyPrefixConstant.IMAGE_TASK + userId);
            List<ImageJobPO> list = new ArrayList<>();
            Collection<Object> values = entries.values();
            for (Object value : values) {
                String json = (String)value;
                ImageJobPO imageJobPO = JSONUtil.toBean(json, ImageJobPO.class);
                list.add(imageJobPO);
            }
            return list;
        }
        LambdaQueryWrapper<ImageJobPO> query = new LambdaQueryWrapper<>();
        query.eq(ImageJobPO::getUserId,userId);
        try {
            query.eq(ImageJobPO::getJobStatus, ImageJobStatus.valueOf(taskStatus));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PARAMS_VALID_FAIL);
        }
        return imageJobMapper.selectList(query);
    }
}
