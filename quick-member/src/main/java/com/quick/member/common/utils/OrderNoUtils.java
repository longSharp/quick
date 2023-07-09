package com.quick.member.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Author: Longcm
 * @Description:
 */
public class OrderNoUtils {
    /**
     * 获取编号
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            result.append(random.nextInt(10));
        }
        return newDate + result;
    }
}
