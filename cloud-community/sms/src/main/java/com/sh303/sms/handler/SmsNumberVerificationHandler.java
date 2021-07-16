package com.sh303.sms.handler;

import com.sh303.sms.dto.SmsPropertiesInfo;
import com.sh303.sms.generator.NumberVerificationCodeGenerator;
import com.sh303.sms.generator.UUIDVerificationKeyGenerator;
import com.sh303.sms.generator.VerificationCodeGenerator;
import com.sh303.sms.generator.VerificationKeyGenerator;
import com.sh303.sms.sms.SmsService;
import com.sh303.sms.store.VerificationStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 短信(数字随机验证码) 处理器
 */

@Slf4j
public class SmsNumberVerificationHandler extends AbstractVerificationHandler {

    private String name;

    private int length;

    private VerificationStore verificationStore;

    private VerificationKeyGenerator verificationKeyGenerator;

    private VerificationCodeGenerator verificationCodeGenerator;

    private SmsService smsService;

    @Autowired
    private SmsPropertiesInfo smsPropertiesInfo;

    /**
     * @param name
     * @param length
     */
    public SmsNumberVerificationHandler(String name, int length) {
        this.name = name;
        this.length = length;
        verificationKeyGenerator = new UUIDVerificationKeyGenerator();
        verificationCodeGenerator = new NumberVerificationCodeGenerator(length);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * 验证信息存储 获取redis容器
     */
    @Override
    public VerificationStore getVerificationStore() {
        return verificationStore;
    }

    /**
     * 验证信息存储 实例化redis容器
     */
    public void setVerificationStore(VerificationStore verificationStore) {
        this.verificationStore = verificationStore;
    }

    /**
     * 获取验证码容器的key生成器接口
     */
    @Override
    public VerificationKeyGenerator getVerificationKeyGenerator() {
        return verificationKeyGenerator;
    }

    /**
     * 获取验证码容器的code生成器接口
     */
    @Override
    public VerificationCodeGenerator getVerificationCodeGenerator() {
        return verificationCodeGenerator;
    }

    /**
     * 获取缓存时间
     */
    @Override
    public int getEffectiveTime() {
        return 300;
    }

    /**
     * 设置短信服务
     */
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    String confusion(Map<String, Object> payload, String key, String code) {
        String mobile = String.valueOf(payload.get("mobile"));

        // 发送短信
        smsService.sendSms(mobile, code, smsPropertiesInfo.getSignName(), smsPropertiesInfo.getVerifyCodeTemplate());

        // 测试使用，在控制台输出验证码
        smsService.sendOnConsole(mobile, key, getEffectiveTime());
        return null;
    }
}
