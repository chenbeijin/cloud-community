package com.sh303.sms.generator;

import java.util.UUID;

/**
 * UUID生成验证码的key
 */

public class UUIDVerificationKeyGenerator implements VerificationKeyGenerator {

    /**
     * 根据前缀 生成验证码的key
     */
    @Override
    public String generator(String prefix) {
        String uuid = UUID.randomUUID().toString();
        return prefix + uuid.replace("-", "");
    }

}
