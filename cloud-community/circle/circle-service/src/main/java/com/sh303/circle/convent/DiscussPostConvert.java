package com.sh303.circle.convent;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.entity.DiscussPost;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 定义 帖子 DTO 和 帖子 POJO 之间的转换规则
 */

@org.mapstruct.Mapper
public interface DiscussPostConvert {

    /**
     * 转换类实例 生成 AccountConvertImpl 实现类 在 target
     */
    DiscussPostConvert INSTANCE = Mappers.getMapper(DiscussPostConvert.class);

    /**
     * 把dto转换成pojo
     * @param discussPostDTO
     * @return
     */
    DiscussPost dto2entity(DiscussPostDTO discussPostDTO);

    /**
     * 把pojo转换成dto
     * @param discussPost
     * @return
     */
    DiscussPostDTO entity2dto(DiscussPost discussPost);

    /**
     * list之间也可以转换， pojo的List转成dto list
     * @param discussPostList
     * @return
     */
    List<DiscussPostDTO> entityList2dtoList(List<DiscussPost> discussPostList);

    /**
     * list之间也可以转换， dto list转成pojo的List
     * @param discussPostDTOList
     * @return
     */
    List<DiscussPost> dtoList2entityList(List<DiscussPostDTO> discussPostDTOList);

}
