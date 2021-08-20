package com.sh303.community.common.interceptor;

import com.sh303.circle.api.MessageService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.community.common.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: cloud-community
 * @description: 信息拦截器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @org.apache.dubbo.config.annotation.Reference
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = hostHolder.getUser();
        if (userDTO != null && modelAndView != null) {
            int letterUnreadCount = messageService.findLetterUnreadCount(userDTO.getId(), null);
            int noticeUnreadCount = messageService.findNoticeUnreadCount(userDTO.getId(), null);
            modelAndView.addObject("allUnreadCount", letterUnreadCount + noticeUnreadCount);
        }
    }
}
