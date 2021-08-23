package com.sh303.circle.service;

import com.sh303.circle.api.LikeService;
import com.sh303.common.cache.Cache;
import com.sh303.common.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

/**
 * @program: cloud-community
 * @description: 点赞服务实现
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@org.apache.dubbo.config.annotation.Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private Cache cache;

    /**
     * create by: Chen Bei Jin
     * description: 点赞
     * create time: 2021/8/23 14:12
     * @param userId       当前用户ID
     * @param entityType   点赞实体类型
     * @param entityId     点赞实体ID
     * @param entityUserId 点赞实体用户ID
     */
    @Override
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        cache.getRedisTemplate().execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 某个实体的赞 的key值
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                // 某个用户的赞 的key值
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                // 判断 key 是否存在 获取 布尔值
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();
                if (isMember) {
                    // 存在删除该 值的 缓存
                    operations.opsForSet().remove(entityLikeKey, userId);
                    // 根据key值 减值
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    // 否则 添加该 值的 缓存
                    operations.opsForSet().add(entityLikeKey, userId);
                    // 根据key值 加值
                    operations.opsForValue().increment(userLikeKey);
                }
                // 执行
                return operations.exec();
            }
        });
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某实体点赞的数量
     * create time: 2021/8/23 14:36
     * @param entityType
     * @param entityId
     */
    @Override
    public long findEntityLikeCount(int entityType, int entityId) {
        // 某个实体的赞
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return cache.sizeSet(entityLikeKey);
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某人对某实体的点赞状态
     * create time: 2021/8/23 14:42
     * @param userId
     * @param entityType
     * @param entityId
     */
    @Override
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 查询某人对某实体的点赞状态
        return cache.isMember(entityLikeKey, userId) ? 1 : 0;
    }

    /**
     * create by: Chen Bei Jin
     * description: 查询某个用户获得的赞
     * create time: 2021/8/23 14:43
     * @param userId
     */
    @Override
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        // 查询某个用户获得的赞
        Integer count = (Integer) cache.get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}
