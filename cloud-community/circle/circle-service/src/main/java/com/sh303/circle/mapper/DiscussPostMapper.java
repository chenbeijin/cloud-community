package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.circle.entity.DiscussPost;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: cloud-community
 * @description: 个人帖子 Mapper
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Repository
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    /**
     * 通过用户ID查询用户帖子
     * @param page
     * @param userId    用户ID
     * @param orderMode
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param("page") Page<DiscussPost> page, @Param("userId") String userId, @Param("orderMode") Integer orderMode);
}
