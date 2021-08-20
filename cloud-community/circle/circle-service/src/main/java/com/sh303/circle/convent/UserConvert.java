package com.sh303.circle.convent;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.entity.DiscussPost;
import com.sh303.circle.entity.User;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 定义 用户 DTO 和 用户 POJO 之间的转换规则
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.mapstruct.Mapper
public interface UserConvert {

    /**
     * 转换类实例 生成 UserConvertImpl 实现类 在 target
     */
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * create by: Chen Bei Jin
     * description: 把dto转换成pojo
     * create time: 2021/8/16 10:32
     * @param userDTO
     */
    User dto2entity(UserDTO userDTO);

    /**
     * create by: Chen Bei Jin
     * description: 把pojo转换成dto
     * create time: 2021/8/16 10:32
     * @param user
     */
    UserDTO entity2dto(User user);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， pojo的List转成dto list
     * create time: 2021/8/16 10:32
     * @param userList
     */
    List<UserDTO> entityList2dtoList(List<User> userList);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， dto list转成pojo的List
     * create time: 2021/8/16 10:32
     * @param userDTOList
     */
    List<User> dtoList2entityList(List<UserDTO> userDTOList);

}
