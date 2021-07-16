package com.sh303.circle.service;

import com.sh303.circle.api.UserService;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.convent.UserConvert;
import com.sh303.circle.entity.User;
import com.sh303.circle.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过用户ID查询用户
     * @param userId 用户ID
     * @return
     */
    @Override
    public UserDTO findUserById(String userId) {
        User user = userMapper.selectById(userId);
        UserDTO userDTO = UserConvert.INSTANCE.entity2dto(user);
        return userDTO;
    }
}
