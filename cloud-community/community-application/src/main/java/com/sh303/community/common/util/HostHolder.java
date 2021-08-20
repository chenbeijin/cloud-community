package com.sh303.community.common.util;

import com.sh303.circle.api.dto.UserDTO;
import org.springframework.stereotype.Component;

/**
 * @program: cloud-community
 * @description: 持有用户信息, 用于代替session对象.
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class HostHolder {

    private ThreadLocal<UserDTO> users = new ThreadLocal<>();

    public void setUser(UserDTO user) {
        users.set(user);
    }

    public UserDTO getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
