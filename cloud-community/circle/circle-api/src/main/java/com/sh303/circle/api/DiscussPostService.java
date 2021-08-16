package com.sh303.circle.api;

import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.common.domain.PageVO;

public interface DiscussPostService {

    /**
     * 通过用户ID查询用户帖子
     * @param userId 用户ID
     * @param offset 页码
     * @param limit  每页显示
     * @return
     */
    PageVO<DiscussPostDTO> findDiscussPosts(String userId, Integer offset, Integer limit, Integer orderMode);

    /**
     * 通过用户ID查询用户帖子评论数
     * @param userId 用户ID
     * @return
     */
    int findDiscussPostsRows(String userId);

}
