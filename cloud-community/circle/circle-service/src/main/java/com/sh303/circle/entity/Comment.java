package com.sh303.circle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @program: cloud-community
 * @description: 用户点评
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体ID
     */
    private Integer entityId;

    /**
     * 目标
     */
    private Integer targetId;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
