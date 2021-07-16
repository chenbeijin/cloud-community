package com.sh303.sms.config;

import com.sh303.sms.handler.AbstractVerificationHandler;
import com.sh303.sms.handler.SmsNumberVerificationHandler;
import com.sh303.sms.sms.cloud.CloudSmsService;
import com.sh303.sms.store.VerificationStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证 业务配置
 */

@Configuration
public class BusinessConfig {

    @Autowired
    private VerificationStore verificationStore;

    @Autowired
    private CloudSmsService cloudSmsService;

    /**
     * 注入短信号码验证处理器
     */
    @Bean
    public SmsNumberVerificationHandler smsNumberVerificationHandler() {
        // 创建短信(数字随机验证码) 处理器
        SmsNumberVerificationHandler smsNumberVerificationHandler = new SmsNumberVerificationHandler("sms", 6);
        // 设置 验证信息存储 实例化redis容器
        smsNumberVerificationHandler.setVerificationStore(verificationStore);
        // 设置短信服务
        smsNumberVerificationHandler.setSmsService(cloudSmsService);
        return smsNumberVerificationHandler;
    }

    /**
     * 验证处理程序映射
     */
    @Bean
    public Map<String, AbstractVerificationHandler> verificationHandlerMap() {
        List<AbstractVerificationHandler> verificationHandlerList = new ArrayList<>();
        verificationHandlerList.add(smsNumberVerificationHandler());

        Map<String, AbstractVerificationHandler> verificationHandlerMap = new HashMap<>();
        for (AbstractVerificationHandler handler : verificationHandlerList) {
            verificationHandlerMap.put(handler.getName(), handler);
        }
        return verificationHandlerMap;
    }

}
