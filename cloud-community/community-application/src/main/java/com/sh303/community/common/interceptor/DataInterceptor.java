package com.sh303.community.common.interceptor;

import com.sh303.circle.api.DataService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.community.common.util.HostHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: cloud-community
 * @description: 日期拦截器
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Reference
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 统计UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        // 统计DAU
        UserDTO userDTO = hostHolder.getUser();
        if (userDTO != null) {
            dataService.recordDAU(userDTO.getId());
        }
        return true;
    }
}
