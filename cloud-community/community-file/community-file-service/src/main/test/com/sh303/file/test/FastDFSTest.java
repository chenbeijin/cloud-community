package com.sh303.file.test;

import com.sh303.file.api.FileService;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {

    @Reference
    private FileService fileService;

    /**
     * 测试上传
     */
    @Test
    public void testUpload() throws IOException {
        // 要上传的文件
        File file = new File("D:\\我的文档\\Pictures\\mqdemo.png");

        FileInputStream input = new FileInputStream(file);

        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/png", IOUtils.toByteArray(input));

        byte[] bytes = multipartFile.getBytes();
        List<Map<String, Object>> maps = fileService.uploadImage(bytes, "mqdemo.png");
        String imageUrl = (String) maps.get(1).get("imageUrl");
        System.out.println(imageUrl);
    }

    /**
     * 测试下载
     */
    @Test
    public void testDownloadFile() {
        // fastdfs服务器文件地址
        String filePath = "group1/M00/00/00/rBn4UmEdvtqAHaFWAAB0ONLX-4I950.png";
        // 获取下载文件的二进制数据
        byte[] bytes = fileService.downloadFile(filePath);

        //变量获取一下文件名称
        String fileName = "rBn4UmEdvtqAHaFWAAB0ONLX-4I950.png";
        //在本地路径创建一个与文件名相同的文件
        OutputStream outputStream = null;
        try {
            // 输出文件流 写到指定目录下
            outputStream = new FileOutputStream("D:\\我的文档\\Pictures\\" + fileName);

            //将文件字节流写入到创建的文件中
            outputStream.write(bytes, 0, bytes.length);
            // 输出流刷新
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试文件删除
     */
    @Test
    public void testDeleteFile() {
        // fastdfs服务器文件地址
        String filePath = "group1/M00/00/00/rBn4UmCeH4CAD9OwAAB0ONLX-4I464.png";
        // 删除文件
        fileService.deleteFile(filePath);
    }
}
