package com.sh303.circle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh303.circle.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
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
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户帖子
     * create time: 2021/8/20 9:22
     * @param page
     * @param userId    用户ID
     * @param orderMode
     */
    List<DiscussPost> selectDiscussPosts(@Param("page") Page<DiscussPost> page, @Param("userId") String userId, @Param("orderMode") Integer orderMode);

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID分页查询用户帖子
     * create time: 2021/8/20 9:22
     * @param limit     分页
     * @param offset    当前页码
     * @param userId    用户ID
     * @param orderMode
     */
    List<DiscussPost> selectDiscussPostPages(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit, @Param("orderMode") int orderMode);

    /**
     * create by: Chen Bei Jin
     * description: 添加帖子
     * create time: 2021/8/20 15:39
     * @param discussPost
     */
    int insertDiscussPost(DiscussPost discussPost);
}
