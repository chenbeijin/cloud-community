package com.sh303.circle.mapper;

import com.sh303.circle.api.dto.CommentDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sh303.circle.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * @program: cloud-community
 * @description: 用户点评 Mapper
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}
