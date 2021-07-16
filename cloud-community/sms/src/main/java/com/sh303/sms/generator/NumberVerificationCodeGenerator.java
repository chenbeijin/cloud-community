package com.sh303.sms.generator;

import java.util.Random;

/**
 * 数字验证码生成
 */

public class NumberVerificationCodeGenerator implements VerificationCodeGenerator {

    /**
     * 验证码长度
     */
    private Integer length;

    /**
     * 数字验证码生成器
     * @param length
     */
    public NumberVerificationCodeGenerator(Integer length) {
        this.length = length;
    }

    /**
     * 验证码生成
     */
    @Override
    public String generator() {
        return this.getNumRandom(length);
    }

    /**
     * 随机生成定义长度的验证码
     * @param length
     * @return
     */
    private String getNumRandom(Integer length) {
        String value = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 随机生成0-9的数字
            value += String.valueOf(random.nextInt(10));
        }
        return value;
    }
}
