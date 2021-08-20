package com.sh303.file.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sh303.file.api.FileService;
import com.sh303.file.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 实现文件服务接口
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Slf4j
@org.apache.dubbo.config.annotation.Service
@EnableConfigurationProperties(UploadProperties.class)
@Component
public class FileServiceImpl implements FileService {

    /**
     * fastDFS Storage客户端
     */
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传 文件类型配置
     */
    @Autowired
    private UploadProperties uploadProperties;

    /**
     * create by: Chen Bei Jin
     * description: 图片上传
     * create time: 2021/8/19 9:39
     * @param fileByte
     */
    @Override
    public List<Map<String, Object>> uploadImage(byte[] fileByte, String fileName) {
        // 传值
        List<Map<String, Object>> results = new ArrayList<>();
        // 返回信息Map
        Map<String, Object> messageMap = new HashMap<>();
        // 文件信息Map
        Map<String, Object> fileMap = new HashMap<>();

        InputStream inputStream = null;
        MultipartFile multipartFile = null;
        try {
            inputStream = new ByteArrayInputStream(fileByte);

            multipartFile = new MockMultipartFile("file", fileName, "image/png", IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1、校验文件类型
        String contentType = multipartFile.getContentType();
        if (!uploadProperties.getAllowTypes().contains(contentType)) {
            messageMap.put("fileMsg", "上传文件类型不是图片类型");
            results.add(messageMap);
            results.add(fileMap);
            return results;
        }
        // 2、校验文件内容
        try {
            // 图片流
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
                messageMap.put("fileMsg", "校验文件内容出错");
                results.add(messageMap);
                results.add(fileMap);
                return results;
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageMap.put("fileMsg", "校验文件内容失败");
            results.add(messageMap);
            results.add(fileMap);
            return results;
        }

        try {
            // 上传到FastDFS
            // 获取扩展名
            String extension = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
            // 上传
            StorePath storePath = fastFileStorageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), extension, null);
            // 返回图片路径
            fileMap.put("imageUrl", uploadProperties.getBaseUrl() + storePath.getFullPath());
            results.add(messageMap);
            results.add(fileMap);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            messageMap.put("fileMsg", "上传文件失败");
            results.add(messageMap);
            results.add(fileMap);
            return results;
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 下载文件
     * create time: 2021/8/19 9:39
     * @param filePath
     */
    @Override
    public byte[] downloadFile(String filePath) {
        byte[] bytes = null;
        // 判断文件名不为空
        if (StringUtils.isNotBlank(filePath)) {
            // 获取组
            String group = filePath.substring(0, filePath.indexOf("/"));
            // 获取地址
            String path = filePath.substring(filePath.indexOf("/") + 1);
            // 创建下载字节数组
            DownloadByteArray byteArray = new DownloadByteArray();
            // 获取文件下载字节
            bytes = fastFileStorageClient.downloadFile(group, path, byteArray);
        }
        return bytes;
    }

    /**
     * create by: Chen Bei Jin
     * description: 删除文件
     * create time: 2021/8/19 9:39
     * @param filePath
     */
    @Override
    public Map<String, Object> deleteFile(String filePath) {
        // 返回信息Map
        Map<String, Object> messageMap = new HashMap<>();
        // 判断文件名不为空
        if (StringUtils.isNotBlank(filePath)) {
            // 删除文件
            try {
                fastFileStorageClient.deleteFile(filePath);
            } catch (Exception e){
                e.printStackTrace();
                messageMap.put("fileMsg", "删除文件失败");
                return messageMap;
            }
        }
        messageMap.put("fileMsg", "删除文件成功");
        return messageMap;
    }
}
