package com.sh303.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: cookie 工具类
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class CookieUtil implements Serializable {

    /**
     * create by: Chen Bei Jin
     * description: 获取cookie值
     * create time: 2021/8/16 17:32
     * @params
     * @return
     */
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
