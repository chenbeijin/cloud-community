package com.sh303.circle.convent;

import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.circle.entity.Comment;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 定义 评论 DTO 和 评论 POJO 之间的转换规则
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.mapstruct.Mapper
public interface CommentConvert {

    /**
     * 转换类实例 生成 CommentConvertImpl 实现类 在 target
     */
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    /**
     * create by: Chen Bei Jin
     * description: 把dto转换成pojo
     * create time: 2021/8/16 10:31
     * @param commentDTO
     */
    Comment dto2entity(CommentDTO commentDTO);

    /**
     * create by: Chen Bei Jin
     * description: 把pojo转换成dto
     * create time: 2021/8/16 10:31
     * @param comment
     */
    CommentDTO entity2dto(Comment comment);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， pojo的List转成dto list
     * create time: 2021/8/16 10:31
     * @param commentList
     */
    List<CommentDTO> entityList2dtoList(List<Comment> commentList);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， dto list转成pojo的List
     * create time: 2021/8/16 10:31
     * @param commentDTOList
     */
    List<Comment> dtoList2entityList(List<CommentDTO> commentDTOList);

}
