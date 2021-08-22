package com.sh303.community.common.interceptor;

import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.LoginTicketDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.common.util.CommunityConstant;
import com.sh303.common.util.CookieUtil;
import com.sh303.community.common.util.HostHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: cloud-community
 * @description: 登录凭证拦截器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class LoginTicketInterceptor implements CommunityConstant, HandlerInterceptor {

    @Reference
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * create by: Chen Bei Jin
     * description: 控制层执行前执行
     * create time: 2021/8/16 17:33
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

         if (ticket != null) {
            // 查询凭证
            LoginTicketDTO loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                UserDTO user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);
                // 构建用户认证的结果，并存入SecurityContext, 以便于Security进行授权
                UserDTO userDTO = userService.findUserById(user.getId());
                List<GrantedAuthority> authorities = new ArrayList<>();
                // 查询某个用户的权限
                authorities.add(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        switch (userDTO.getType()) {
                            case 1:
                                return AUTHORITY_ADMIN;
                            case 2:
                                return AUTHORITY_MODERATOR;
                            default:
                                return AUTHORITY_USER;
                        }
                    }
                });
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }

        return true;
    }

    /**
     * create by: Chen Bei Jin
     * description: 控制层执行后执行
     * create time: 2021/8/16 17:33
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserDTO user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * create by: Chen Bei Jin
     * description: 控制层执行完成后执行
     * create time: 2021/8/16 17:33
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
