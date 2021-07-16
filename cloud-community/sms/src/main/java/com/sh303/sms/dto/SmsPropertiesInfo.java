package com.sh303.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信配置文件
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsPropertiesInfo {

    /**
     * 访问密钥Id
     */
    private String accessKeyId;

    /**
     * 访问密钥的字段机密
     */
    private String accessKeySecret;

    /**
     * 签名
     */
    private String signName;

    /**
     * 验证代码模板
     */
    private String verifyCodeTemplate;

}
