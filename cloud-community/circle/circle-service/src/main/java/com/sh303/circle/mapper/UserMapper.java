package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @program: cloud-community
 * @description: 用户 Mapper
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Repository
public interface UserMapper extends BaseMapper<User> {

}
