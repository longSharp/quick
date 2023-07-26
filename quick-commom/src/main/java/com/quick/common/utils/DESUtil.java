package com.quick.common.utils;

import com.quick.common.enums.ResultCode;
import com.quick.common.exception.BusinessException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 加密协议工具
 */
public class DESUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY = "2EuA2XVvL5J9K8TK";
    private static final String IV = "2EuA2XVvL5J9L5J9";
    private static final SecretKeySpec keySpec;
    private static final IvParameterSpec ivSpec;

    static {
        keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        ivSpec = new IvParameterSpec(IV.getBytes());
    }

    /**
     * 加密
     * @param data
     * @return
     */
    public static String encrypt(String data){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(encryptedBytes);
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
    }

    /**
     * 解密
     * @param data
     * @return
     */
    public static String decrypt(String data){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            // 对加密后的数据进行Base64解码
            byte[] encryptedBytes = Base64.decodeBase64(data);
            byte[] decrypt = cipher.doFinal(encryptedBytes);
            return new String(decrypt, StandardCharsets.UTF_8);
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
    }
}