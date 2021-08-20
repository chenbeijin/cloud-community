package com.sh303.circle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @program: cloud-community
 * @description: 个人帖子
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@TableName("discuss_post")
public class DiscussPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     * 0-普通; 1-置顶;
     */
    private Integer type;

    /**
     * 状态
     * 0-正常; 1-精华; 2-拉黑;
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 评价
     */
    private Double score;

}
