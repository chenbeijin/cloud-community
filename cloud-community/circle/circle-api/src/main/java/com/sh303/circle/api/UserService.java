package com.sh303.circle.api;

import com.sh303.circle.api.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 通过用户ID查询用户
     * @param userId 用户ID
     * @return
     */
    UserDTO findUserById(Integer userId);

    /**
     * 用户注册
     * @param userDTO 用户信息
     * @return
     */
    List<Map<String, Object>> register(UserDTO userDTO);

    /**
     * 用户激活
     * @param userId 用户ID
     * @param code   激活编码
     * @return
     */
    int activation(int userId, String code);
}
