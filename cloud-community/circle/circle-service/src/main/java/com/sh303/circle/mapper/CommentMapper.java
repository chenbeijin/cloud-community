package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.circle.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 用户点评 Mapper
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * create by: Chen Bei Jin
     * description: 分页查询帖子评论
     * create time: 2021/8/21 17:07
     */
    List<Comment> selectCommentsPageByEntity(@Param("page") Page<CommentDTO> page, @Param("entityType") Integer entityType, @Param("entityId") Integer entityId);
}
