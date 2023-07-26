package com.quick.common.service;

import com.quick.common.dto.req.AppSession;

import java.util.Set;

public interface ISessionCache {
    /**
     * 返回Session，如果没有Session则新建一个。
     *
     * @param sessionId sessionId
     * @return Session
     */
    AppSession getSession(String sessionId);

    /**
     * 保存一个Session。
     * @param session session
     */
    AppSession putSession(AppSession session);

    /**
     * 保存一个Session。
     * @param session session
     */
    AppSession putSessionForHash(AppSession session);

    /**
     * 返回Session，如果没有Session则新建一个。
     *
     * @param sessionId sessionId
     * @return Session
     */
    AppSession getSessionForHash(String userId,String ip);


    /**
     * 清除某个用户下的Session。
     *
     * @param userId sessionId
     */
    void clearSessionForHash(String userId);

    /**
     * 清除Session。
     *
     * @param sessionId sessionId
     */
    void clearSession(String sessionId);

    /**
     * 获取所有SessionId。
     *
     * @param sessionId sessionId
     */
    Set<Object> getAllSessionId();
}
