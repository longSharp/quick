package com.quick.member.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quick.member.common.config.params.ServiceParamsConfig;
import com.quick.member.common.constant.AuthServerConstant;
import com.quick.member.domain.dto.req.AppSession;
import com.quick.member.service.ISessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SessionRedisCacheImpl implements ISessionCache {

    @Autowired
    private ServiceParamsConfig serviceParamsConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Gson gson = new GsonBuilder().create();

    @Override
    public AppSession getSession(String sessionId) {
        String jsonStr = stringRedisTemplate.opsForValue().get(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" + sessionId);
        if (StringUtils.hasText(jsonStr)) {
            return gson.fromJson(jsonStr,AppSession.class);
        }
        return null;
    }

    @Override
    public AppSession putSession(AppSession session) {
        String jsonStr = gson.toJson(session);
        stringRedisTemplate.opsForValue().set(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" + session.getId(), jsonStr, serviceParamsConfig.getLoginLockTime(), TimeUnit.MINUTES);
        return session;
    }

    @Override
    public AppSession putSessionForHash(AppSession session) {
        String jsonStr = gson.toJson(session);
        stringRedisTemplate.opsForHash().put(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" + session.getUserId(),session.getIp(),jsonStr);
        stringRedisTemplate.expire(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" + session.getUserId(),serviceParamsConfig.getLoginLockTime(), TimeUnit.MINUTES);
        return session;
    }

    @Override
    public AppSession getSessionForHash(String userId, String ip) {
        String jsonStr = (String) stringRedisTemplate.opsForHash().get(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" +userId,ip);
        if (StringUtils.hasText(jsonStr)) {
            return gson.fromJson(jsonStr,AppSession.class);
        }
        return null;
    }

    @Override
    public void clearSessionForHash(String userId) {
        Set<Object> hashKeys = stringRedisTemplate.opsForHash().keys(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" +userId);
        if(hashKeys.size() == 0) return;
        stringRedisTemplate.opsForHash().delete(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":"+ userId, hashKeys.toArray());
    }

    @Override
    public void clearSession(String sessionId) {
        stringRedisTemplate.delete(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":" + sessionId);
    }

    @Override
    public Set<Object> getAllSessionId() {
        Set<String> keys = stringRedisTemplate.keys(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX + ":*");
        Set<Object> allSessionIds = new HashSet<>();
        if (keys == null) {
            return allSessionIds;
        }
        return keys.stream().map(key -> key.substring(AuthServerConstant.AUTH_SESSION_CACHE_PREFIX.length()+1)).collect(Collectors.toSet());
    }
}
