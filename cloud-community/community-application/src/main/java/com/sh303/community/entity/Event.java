package com.sh303.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: cloud-community
 * @description: 发送系统通知.
 * 将set方法的返回值改为EventBo，方便重复引用此类.
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

public class Event {

    /**
     * 主题
     */
    private String topic;

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 实体类型
     */
    private int entityType;

    /**
     * 实体ID
     */
    private int entityId;

    /**
     * 实体用户ID
     */
    private int entityUserId;

    /**
     * 数据
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * create by: Chen Bei Jin
     * description: 获取主题
     * create time: 2021/8/20 10:27
     */
    public String getTopic() {
        return topic;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置主题
     * create time: 2021/8/20 10:27
     */
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取用户ID
     * create time: 2021/8/20 10:27
     */
    public int getUserId() {
        return userId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置用户ID
     * create time: 2021/8/20 10:27
     */
    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取实体类型
     * create time: 2021/8/20 10:28
     */
    public int getEntityType() {
        return entityType;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置实体类型
     * create time: 2021/8/20 10:28
     */
    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取实体ID
     * create time: 2021/8/20 10:28
     */
    public int getEntityId() {
        return entityId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置实体ID
     * create time: 2021/8/20 10:28
     */
    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取实体用户ID
     * create time: 2021/8/20 10:28
     */
    public int getEntityUserId() {
        return entityUserId;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置实体用户ID
     * create time: 2021/8/20 10:28
     */
    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    /**
     * create by: Chen Bei Jin
     * description: 获取数据
     * create time: 2021/8/20 10:28
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * create by: Chen Bei Jin
     * description: 设置数据
     * create time: 2021/8/20 10:28
     */
    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
