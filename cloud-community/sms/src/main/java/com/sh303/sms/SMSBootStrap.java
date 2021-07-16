package com.sh303.sms;

import com.sh303.sms.dto.SmsPropertiesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 短信模块启动模块
 */

@SpringBootApplication
public class SMSBootStrap {

    /**
     * 获取 nacos 配置中心
     * 配置中心 修改时 也会 动态获取 配置
     */
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SMSBootStrap.class, args);
    }

    /**
     * 短信属性信息
     * 注入到bean 容器
     */
    @Bean
    public SmsPropertiesInfo smsPropertiesInfo() {
        SmsPropertiesInfo smsPropertiesInfo = new SmsPropertiesInfo();
        smsPropertiesInfo.setAccessKeyId(configurableApplicationContext.getEnvironment().getProperty("sms.cloud.accessKeyId"));
        smsPropertiesInfo.setAccessKeySecret(configurableApplicationContext.getEnvironment().getProperty("sms.cloud.accessKeySecret"));
        smsPropertiesInfo.setSignName(configurableApplicationContext.getEnvironment().getProperty("sms.cloud.signName"));
        smsPropertiesInfo.setVerifyCodeTemplate(configurableApplicationContext.getEnvironment().getProperty("sms.cloud.verifyCodeTemplate"));
        return smsPropertiesInfo;
    }

}
