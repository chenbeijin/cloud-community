package com.sh303.community.controller;

import com.google.code.kaptcha.Producer;
import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CommunityUtil;
import com.sh303.common.util.RedisKeyUtil;
import com.sh303.community.common.util.EmailClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 登录控制器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Reference
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${cloud-community.path.domain}")
    private String domain;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailClient mailClient;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private Cache cache;

    /**
     * create by: Chen Bei Jin
     * description: 注册页面
     * create time: 2021/8/16 9:19
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * create by: Chen Bei Jin
     * description: 登录页面
     * create time: 2021/8/16 9:19
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }

    /**
     * create by: Chen Bei Jin
     * description: 提交注册信息
     * create time: 2021/8/16 9:19
     */
    @PostMapping("/register")
    public String register(Model model, UserDTO userDTO) {
        List<Map<String, Object>> results = userService.register(userDTO);
        // 判断是否有错误参数 List 的 1 是 数据 0 是 错误数据
        if (results.get(1).size() != 0 || !results.get(1).isEmpty()) {
            // 获取用户数据
            userDTO = (UserDTO) results.get(1).get("userDTO");
            // 激活邮件
            Context context = new Context();
            context.setVariable("email", userDTO.getEmail());
            // http://localhost/cloud-community/activation/101/code
            // 设置激活模板
            String url = domain + contextPath + "/activation/" + userDTO.getId() + "/" + userDTO.getActivationCode();
            context.setVariable("url", url);
            String content = templateEngine.process("/mail/activation", context);
            // 发送邮箱激活页面
            mailClient.sendMail(userDTO.getEmail(), "激活账号", content);

            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/login");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", results.get(0).get("usernameMsg"));
            model.addAttribute("passwordMsg", results.get(0).get("passwordMsg"));
            model.addAttribute("emailMsg", results.get(0).get("emailMsg"));
            return "/site/register";
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 邮件激活
     * create time: 2021/8/16 15:19
     */
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        // 把用户添加到缓存
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取验证码
     * create time: 2021/8/16 15:20
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        /* 验证码的归属 */
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        // cookie的生成时间
        cookie.setMaxAge(60);
        // 有效路径
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        // 将验证码存入redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        cache.set(redisKey, text, 60);

        // 讲图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 用户登录
     * create time: 2021/8/16 16:29
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
        // 检查验证码
        String kaptcha = null;
        // 判断cookie是否不为空
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            // 不为空值时获取 验证码
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            // 给检查验证码赋值
            kaptcha = (String) cache.get(redisKey);
        }

        // 判断验证码是否正确
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }

        // 检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 退出登录
     * create time: 2021/8/16 17:05
     * @param ticket 登录记录
     */
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
