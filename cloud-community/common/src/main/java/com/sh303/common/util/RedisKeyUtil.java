package com.sh303.common.util;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: 生成redisKey工具
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class RedisKeyUtil implements Serializable {

    /**
     * 分隔符
     */
    private static final String SPLIT = ":";

    /**
     * 实体的赞
     */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 用户的赞
     */
    private static final String PREFIX_USER_LIKE = "like:user";

    /**
     * 关注的目标
     */
    private static final String PREFIX_FOLLOWEE = "followee";

    /**
     * 关注的粉丝
     */
    private static final String PREFIX_FOLLOWER = "follower";

    /**
     * 登陆验证码
     */
    private static final String PREFIX_KAPTCHA = "kaptcha";

    /**
     * 登陆的凭证
     */
    private static final String PREFIX_TICKET = "ticket";

    /**
     * 登录的用户
     */
    private static final String PREFIX_USER = "user";

    /**
     * 单日uv
     */
    private static final String PREFIX_UV = "uv";

    /**
     * 区间uv
     */
    private static final String PREFIX_DAU = "dau";

    /**
     * 帖子分数
     */
    private static final String PREFIX_POST = "post";

    /**
     * create by: Chen Bei Jin
     * description: 某个实体的赞
     * like:entity:entityType:entityId -> set(userId)
     * create time: 2021/8/16 14:19
     * @param entityType
     * @param entityId
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 某个用户的赞
     * like:user:userId -> int
     * create time: 2021/8/16 14:20
     * @param userId
     */
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 某个用户关注的实体
     * followee:userId:entityType -> zset(entityId,now)
     * create time: 2021/8/16 14:20
     * @param userId
     * @param entityType
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * create by: Chen Bei Jin
     * description: 某个实体拥有的粉丝
     * follower:entityType:entityId -> zset(userId,now)
     * create time: 2021/8/16 14:21
     * @param entityType
     * @param entityId
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 登陆验证码
     * create time: 2021/8/16 14:21
     * @param owner
     */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * create by: Chen Bei Jin
     * description: 登陆的凭证
     * create time: 2021/8/16 14:21
     * @param ticket
     */
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /**
     * create by: Chen Bei Jin
     * description: 用户
     * create time: 2021/8/16 14:21
     * @param userId
     */
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 单日UV
     * create time: 2021/8/16 14:22
     * @param date
     */
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    /**
     * create by: Chen Bei Jin
     * description: 区间UV
     * create time: 2021/8/16 14:22
     * @param startDate
     * @param endDate
     */
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * create by: Chen Bei Jin
     * description: 单日活跃用户
     * create time: 2021/8/16 14:22
     * @param date
     */
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    /**
     * create by: Chen Bei Jin
     * description: 单日区间活跃用户
     * create time: 2021/8/16 14:22
     * @param startDate
     * @param endDate
     */
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * create by: Chen Bei Jin
     * description: 帖子分数
     * create time: 2021/8/16 14:22
     */
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}
