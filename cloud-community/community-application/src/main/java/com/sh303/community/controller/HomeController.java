package com.sh303.community.controller;

import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.domain.Page;
import com.sh303.common.domain.PageVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 主页控制器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Controller
public class HomeController {

    @Reference
    private DiscussPostService discussPostService;

    @Reference
    private UserService userService;

    /**
     * create by: Chen Bei Jin
     * description: 首页初始化帖子数据 进行分页
     * create time: 2021/8/21 8:47
     */
    @GetMapping("/index")
    public String getIndexPage(Model model, Page page, @RequestParam(name = "orderMode", defaultValue = "0") Integer orderMode) {
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        // 设置分页总页码
        page.setRows(discussPostService.findDiscussPostsRows("0"));
        // 设置路径
        page.setPath("/index");

        // 获取分页帖子对象
        PageVO<DiscussPostDTO> list = discussPostService.findDiscussPosts("0", page.getCurrent(), page.getLimit(), orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            // 写入返回数据
            for (DiscussPostDTO discussPostDTO : list.getItems()) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPostDTO);
                // 根据帖子用户ID 添加用户数据
                UserDTO userDTO = userService.findUserById(discussPostDTO.getUserId());
                map.put("user", userDTO);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        return "/index";
    }

}
