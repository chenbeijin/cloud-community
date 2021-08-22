package com.sh303.circle.api;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.common.domain.PageVO;

/**
 * @program: cloud-community
 * @description: 个人帖子服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface DiscussPostService {

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户帖子
     * create time: 2021/8/19 9:07
     * @param userId 用户ID
     * @param offset 页码
     * @param limit  每页显示
     */
    PageVO<DiscussPostDTO> findDiscussPosts(String userId, Integer offset, Integer limit, Integer orderMode);

    /**
     * create by: Chen Bei Jin
     * description: 通过用户ID查询用户帖子评论数
     * create time: 2021/8/19 9:08
     * @param userId 用户ID
     */
    int findDiscussPostsRows(String userId);

    /**
     * create by: Chen Bei Jin
     * description: 发布评论
     * create time: 2021/8/20 9:38
     * @param discussPostDTO
     */
    int addDiscussPost(DiscussPostDTO discussPostDTO);

    /**
     * create by: Chen Bei Jin
     * description: 查询帖子
     * create time: 2021/8/21 8:53
     * @param id
     */
    DiscussPostDTO findDiscussPostById(int id);

    /**
     * create by: Chen Bei Jin
     * description: 修改评论数量
     * create time: 2021/8/21 14:51
     * @param id           帖子id
     * @param commentCount 评论数量
     */
    int updateCommentCount(int id, int commentCount);

}
