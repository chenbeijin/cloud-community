package com.sh303.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 上传 文件类型配置
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    /**
     * 件服务器地址
     */
    private String baseUrl;

    /**
     * 文件类型
     */
    private List<String> allowTypes;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(List<String> allowTypes) {
        this.allowTypes = allowTypes;
    }
}
