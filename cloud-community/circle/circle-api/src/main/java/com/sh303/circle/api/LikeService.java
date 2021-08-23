package com.sh303.circle.api;

/**
 * @program: cloud-community
 * @description: 点赞服务
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public interface LikeService {

    /**
     * create by: Chen Bei Jin
     * description: 点赞
     * create time: 2021/8/23 14:12
     * @param userId       当前用户ID
     * @param entityType   点赞实体类型
     * @param entityId     点赞实体ID
     * @param entityUserId 点赞实体用户ID
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * create by: Chen Bei Jin
     * description: 查询某实体点赞的数量
     * create time: 2021/8/23 14:36
     * @param entityType
     * @param entityId
     */
    long findEntityLikeCount(int entityType, int entityId);

    /**
     * create by: Chen Bei Jin
     * description: 查询某人对某实体的点赞状态
     * create time: 2021/8/23 14:42
     * @param userId
     * @param entityType
     * @param entityId
     */
    int findEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * create by: Chen Bei Jin
     * description: 查询某个用户获得的赞
     * create time: 2021/8/23 14:43
     * @param userId
     */
    int findUserLikeCount(int userId);
}
