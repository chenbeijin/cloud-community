package com.sh303.circle.api;

import com.sh303.circle.api.dto.LoginTicketDTO;
import com.sh303.circle.api.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 用户服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface UserService {

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户
     * create time: 2021/8/16 9:20
     * @param userId 用户ID
     */
    UserDTO findUserById(Integer userId);

    /**
     * create by: Chen Bei Jin
     * description: 用户注册
     * create time: 2021/8/16 9:19
     * @param userDTO 用户信息
     */
    List<Map<String, Object>> register(UserDTO userDTO);

    /**
     * create by: Chen Bei Jin
     * description: 用户激活
     * create time: 2021/8/16 9:19
     * @param userId 用户ID
     * @param code   激活编码
     */
    int activation(int userId, String code);

    /**
     * create by: Chen Bei Jin
     * description: 用户登录
     * create time: 2021/8/16 16:20
     * @param username       用户账号
     * @param password       密码
     * @param expiredSeconds 过期时间
     */
    Map<String, Object> login(String username, String password, long expiredSeconds);

    /**
     * create by: Chen Bei Jin
     * description: 退出登录
     * create time: 2021/8/16 17:05
     * @param ticket
     */
    void logout(String ticket);

    /**
     * create by: Chen Bei Jin
     * description: 查询登录
     * create time: 2021/8/16 17:34
     * @param ticket 凭证
     */
    LoginTicketDTO findLoginTicket(String ticket);

    /**
     * create by: Chen Bei Jin
     * description: 更新头像
     * create time: 2021/8/19 10:34
     */
    int updateHeader(int userId, String headerUrl);
}
