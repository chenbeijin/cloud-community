package com.sh303.circle.api;

import com.sh303.circle.api.dto.CommentDTO;
import com.sh303.common.domain.PageVO;

/**
 * @program: cloud-community
 * @description: 评论服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface CommentService {

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的信息
     * create time: 2021/8/21 14:31
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     */
    PageVO<CommentDTO> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的回复数量
     * create time: 2021/8/21 14:31
     * @param entityType
     * @param entityId
     */
    int findCommentCount(int entityType, int entityId);

    /**
     * create by: Chen Bei Jin
     * description: 添加帖子的回复信息
     * create time: 2021/8/21 14:31
     * @param commentDTO
     */
    int addComment(CommentDTO commentDTO);

    /**
     * create by: Chen Bei Jin
     * description: 获取触发的评论事件
     * create time: 2021/8/21 14:32
     * @param id
     */
    CommentDTO findCommentById(int id);

    /**
     * create by: Chen Bei Jin
     * description: 获取帖子的回复数量
     * create time: 2021/8/21 14:32
     * @param id
     */
    int findCommentCountById(int id);

    /**
     * create by: Chen Bei Jin
     * description: 获取用户所有的回帖
     * create time: 2021/8/21 15:12
     * @param id
     * @param offset
     * @param limit
     */
    PageVO<CommentDTO> findCommentsByUserId(Integer id, Integer offset, Integer limit);
}
