package com.quick.draw.domain.dto.req;

public class UserHolder {

    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static Long getUserId() {
        return userThreadLocal.get();
    }

    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }

    public static void removeUserId() {
        userThreadLocal.remove();
    }
}
