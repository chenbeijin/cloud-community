package com.sh303.circle.api;

import com.sh303.circle.api.dto.UserDTO;

public interface UserService {

    /**
     * 通过用户ID查询用户
     * @param userId 用户ID
     * @return
     */
    UserDTO findUserById(String userId);

}
