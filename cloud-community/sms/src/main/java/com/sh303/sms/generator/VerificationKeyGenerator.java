package com.sh303.sms.generator;

/**
 * 验证码的key生成器接口
 */

public interface VerificationKeyGenerator {

    /**
     * 根据前缀 生成验证码的key
     */
    String generator(String prefix);

}
