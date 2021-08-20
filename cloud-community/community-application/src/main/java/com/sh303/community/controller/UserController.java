package com.sh303.community.controller;

import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.annotation.LoginRequired;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.common.util.RedisKeyUtil;
import com.sh303.community.common.util.HostHolder;
import com.sh303.file.api.FileService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.logging.log4j.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

   /* @Reference
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;*/

    @Reference
    private FileService fileService;

    /**
     * create by: Chen Bei Jin
     * description: 上传文件
     * create time: 2021/8/19 9:24
     */
    @GetMapping(path = "/setting")
    public String getSettingPage(Model model) {
        return "/site/setting";
    }

    /**
     * 更换用户头像
     * @param fileName
     * @return
     */
    /*@RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "文件名不能为空!");
        }

        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

        return CommunityUtil.getJSONString(0);
    }*/

    /**
     * create by: Chen Bei Jin
     * description: 将用户头像下载到服务器
     * create time: 2021/8/19 10:51
     */
    @SneakyThrows
    @PostMapping(path = "/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }
        // 文件上传到服务器 返回消息
        byte[] headerImageByte = headerImage.getBytes();

        List<Map<String, Object>> results = fileService.uploadImage(headerImageByte, fileName);

        // 判断是否有错误参数 List 的 1 是 数据 0 是 错误数据
        if (results.get(1).size() != 0 || !results.get(1).isEmpty()) {
            // 获取 头像文件地址
            String imageUrl = (String) results.get(1).get("imageUrl");
            // 获取 线程中 缓存的 用户
            UserDTO userDTO = hostHolder.getUser();
            // 更新用户头像
            userService.updateHeader(userDTO.getId(), imageUrl);
            return "redirect:/index";
        } else {
            model.addAttribute("fileMsg", results.get(0).get("fileMsg"));
            return "/site/setting";
        }
    }

    /**
     * 已废弃
     * 读取本地用户头像
     * @param fileName
     * @param response
     */
    /*@RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }*/
}
