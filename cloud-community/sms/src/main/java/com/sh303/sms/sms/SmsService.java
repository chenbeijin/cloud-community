package com.sh303.sms.sms;

/**
 * 短信服务接口
 */

public interface SmsService {

    /**
     * 发送短信
     * @param mobile 移动电话
     * @param code   验证码
     */
    default void sendSms(String mobile, String code) {

    }

    /**
     * 发送短信验证码
     * @param mobile   移动电话
     * @param code     验证码
     * @param signName 签的名字
     * @param template 模板
     */
    void sendSms(String mobile, String code, String signName, String template);

    /**
     * 在控制台输出验证码，模拟发送短信
     * @param mobile        移动电话
     * @param code          验证码
     * @param effectiveTime 缓存时间
     */
    void sendOnConsole(String mobile, String code, int effectiveTime);

}
