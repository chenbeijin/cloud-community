package com.sh303.circle.convent;

import com.sh303.circle.api.dto.MessageDTO;
import com.sh303.circle.api.dto.UserDTO;
import com.sh303.circle.entity.Message;
import com.sh303.circle.entity.User;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 定义 消息 DTO 和 消息 POJO 之间的转换规则
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.mapstruct.Mapper
public interface MessageConvert {

    /**
     * 转换类实例 生成 MessageConvertImpl 实现类 在 target
     */
    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    /**
     * create by: Chen Bei Jin
     * description: 把dto转换成pojo
     * create time: 2021/8/16 10:32
     * @param messageDTO
     */
    Message dto2entity(MessageDTO messageDTO);

    /**
     * create by: Chen Bei Jin
     * description: 把pojo转换成dto
     * create time: 2021/8/16 10:32
     * @param message
     */
    MessageDTO entity2dto(Message message);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， pojo的List转成dto list
     * create time: 2021/8/16 10:32
     * @param messageList
     */
    List<MessageDTO> entityList2dtoList(List<Message> messageList);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， dto list转成pojo的List
     * create time: 2021/8/16 10:32
     * @param messageDTOList
     */
    List<Message> dtoList2entityList(List<MessageDTO> messageDTOList);

}
