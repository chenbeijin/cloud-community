package com.sh303.sms.handler;

import com.sh303.sms.dto.VerificationInfo;
import com.sh303.sms.generator.VerificationCodeGenerator;
import com.sh303.sms.generator.VerificationKeyGenerator;
import com.sh303.sms.store.VerificationStore;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 抽象的验证码处理接口
 */

public abstract class AbstractVerificationHandler {

    /**
     * phone手机号码字段
     */
    public abstract String getName();

    /**
     * 获取缓存容器
     */
    public abstract VerificationStore getVerificationStore();

    /**
     * 获取验证码key值
     */
    public abstract VerificationKeyGenerator getVerificationKeyGenerator();

    /**
     * 获取验证码值
     */
    public abstract VerificationCodeGenerator getVerificationCodeGenerator();

    /**
     * 缓存时间值
     */
    public abstract int getEffectiveTime();

    /**
     * 执行混淆动作：
     * 举例：
     * 1.生成混淆图片
     * 2.发送短信验证码
     * 3.发送邮件验证码
     * 4.生成邮件链接附加参数，并发送邮件
     * ...
     * @return 混淆后内容
     * 举例：
     * 1.图片验证码为:图片base64编码
     * 2.短信验证码为:null
     * 3.邮件验证码为: null
     * 4.邮件链接点击验证为：url附加验证参数信息
     */
    abstract String confusion(Map<String, Object> payload, String key, String code);

    /**
     * 生成验证信息
     * @param payload       业务携带参数，如手机号 ，邮箱
     * @param effectiveTime 验证信息有效时间(秒)
     * @return
     */
    public VerificationInfo getVerificationInfo(Map<String, Object> payload, int effectiveTime) {
        // 判断是否有传缓存时间
        effectiveTime = effectiveTime > 0 ? effectiveTime : getEffectiveTime();
        // 生成key值
        String key = getVerificationKeyGenerator().generator(getName());
        // 生成code
        String code = getVerificationCodeGenerator().generator();
        // 封装内容
        String content = confusion(payload, key, code);
        // 添加到redis中
        getVerificationStore().set(key, code, effectiveTime);
        return new VerificationInfo(key, content);
    }

    /**
     * 验证码校验
     * @param verificationKey  验证key
     * @param verificationCode 验证码
     * @return
     */
    public boolean verify(String verificationKey, String verificationCode) {
        // 判断传值是否为空
        if (StringUtils.isBlank(verificationKey) || StringUtils.isBlank(verificationCode)) {
            return false;
        }
        // 判断验证码是否存在
        String code = getVerificationStore().get(verificationKey);
        if (StringUtils.isBlank(code)) {
            return false;
        }
        // 判断验证码是否相同
        return code.equals(verificationCode);
    }

}
