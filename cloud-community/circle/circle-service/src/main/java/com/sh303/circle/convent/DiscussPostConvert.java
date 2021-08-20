package com.sh303.circle.convent;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.entity.DiscussPost;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 定义 帖子 DTO 和 帖子 POJO 之间的转换规则
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.mapstruct.Mapper
public interface DiscussPostConvert {

    /**
     * 转换类实例 生成 DiscussPostConvertImpl 实现类 在 target
     */
    DiscussPostConvert INSTANCE = Mappers.getMapper(DiscussPostConvert.class);

    /**
     * create by: Chen Bei Jin
     * description: 把dto转换成pojo
     * create time: 2021/8/16 10:31
     * @param discussPostDTO
     */
    DiscussPost dto2entity(DiscussPostDTO discussPostDTO);

    /**
     * create by: Chen Bei Jin
     * description: 把pojo转换成dto
     * create time: 2021/8/16 10:31
     * @param discussPost
     */
    DiscussPostDTO entity2dto(DiscussPost discussPost);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， pojo的List转成dto list
     * create time: 2021/8/16 10:31
     * @param discussPostList
     */
    List<DiscussPostDTO> entityList2dtoList(List<DiscussPost> discussPostList);

    /**
     * create by: Chen Bei Jin
     * description: list之间也可以转换， dto list转成pojo的List
     * create time: 2021/8/16 10:31
     * @param discussPostDTOList
     */
    List<DiscussPost> dtoList2entityList(List<DiscussPostDTO> discussPostDTOList);

}
