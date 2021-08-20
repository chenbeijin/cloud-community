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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page, @RequestParam(name = "orderMode", defaultValue = "0") Integer orderMode) {
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostsRows("0"));
        page.setPath("/index");

        PageVO<DiscussPostDTO> list = discussPostService.findDiscussPosts("0", page.getCurrent(), page.getLimit(), orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPostDTO discussPostDTO : list.getItems()) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPostDTO);
                UserDTO userDTO = userService.findUserById(Integer.parseInt(discussPostDTO.getUserId()));
                map.put("user", userDTO);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        return "/index";
    }

}
