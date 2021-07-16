package com.sh303.circle.convent;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.entity.DiscussPost;
import com.sh303.circle.entity.User;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 定义 用户 DTO 和 用户 POJO 之间的转换规则
 */

@org.mapstruct.Mapper
public interface UserConvert {

    /**
     * 转换类实例 生成 AccountConvertImpl 实现类 在 target
     */
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * 把dto转换成pojo
     * @param userDTO
     * @return
     */
    User dto2entity(UserDTO userDTO);

    /**
     * 把pojo转换成dto
     * @param user
     * @return
     */
    UserDTO entity2dto(User user);

    /**
     * list之间也可以转换， pojo的List转成dto list
     * @param userList
     * @return
     */
    List<UserDTO> entityList2dtoList(List<User> userList);

    /**
     * list之间也可以转换， dto list转成pojo的List
     * @param userDTOList
     * @return
     */
    List<User> dtoList2entityList(List<UserDTO> userDTOList);

}
