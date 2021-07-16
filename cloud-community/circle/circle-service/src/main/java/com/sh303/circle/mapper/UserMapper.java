package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.entity.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-07-14
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
