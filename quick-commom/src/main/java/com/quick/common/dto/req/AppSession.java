package com.quick.common.dto.req;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * AppSession保持器.
 * @author longcm
 *
 */
public final class AppSession implements Serializable {

    private static final long serialVersionUID = 3978422529196962354L;

    private String id;

    private String ip;

    private String userId;

    private Integer role;

    private long creationTime = System.currentTimeMillis();

    private long lastAccessedTime = 0;

    private final Map<String, Object> map = new HashMap<String, Object>();

    private boolean m_new = true;

    private AppSession() {
    }

    /**
     * getCreationTime.
     *
     * @return long
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * getId.
     *
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * getLastAccessedTime.
     *
     * @return long
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * updateLastAccessedTime.
     */
    public void updateLastAccessedTime() {
        this.lastAccessedTime = System.currentTimeMillis();
        this.m_new = false;
    }

    // ******************************************************

    /**
     * getAttribute.
     *
     * @param key
     *            String
     * @return Object
     */
    public Object getAttribute(String key) {
        return map.get(key);
    }

    /**
     * setAttribute.
     *
     * @param key
     *            String
     * @param value
     *            Object
     */
    public void setAttribute(String key, Object value) {
        map.put(key, value);
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * removeAttribute.
     *
     * @param key
     *            String
     */
    public void removeAttribute(String key) {
        map.remove(key);
    }

    /**
     * isNew.
     *
     * @return boolean
     */
    public boolean isNew() {
        return m_new;
    }

    /**
     * check.
     *
     *            Object
     * @return ApplicationSession
     */
    public static AppSession create(String sessionId) {
        AppSession session = new AppSession();
        session.m_new = true;
        session.id = sessionId;
        return session;
    }

    public static AppSession create(String remoteIp,String userId,String sessionId) {
        AppSession session = new AppSession();
        session.ip = remoteIp;
        session.m_new = true;
        session.id = sessionId;
        session.userId = userId;
        return session;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }
}
