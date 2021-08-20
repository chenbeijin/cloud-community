package com.sh303.file.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 文件服务接口
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface FileService {

    /**
     * create by: Chen Bei Jin
     * description: 图片上传
     * create time: 2021/8/19 9:39
     * @param fileByte
     */
    List<Map<String, Object>> uploadImage(byte[] fileByte, String fileName);

    /**
     * create by: Chen Bei Jin
     * description: 下载文件
     * create time: 2021/8/19 9:39
     * @param filePath
     */
    byte[] downloadFile(String filePath);

    /**
     * create by: Chen Bei Jin
     * description: 删除文件
     * create time: 2021/8/19 9:39
     * @param filePath
     */
    Map<String, Object> deleteFile(String filePath);

}
