package com.sh303.circle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @program: cloud-community
 * @description: 信息
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 开始ID
     */
    private Integer fromId;

    /**
     * 到ID
     */
    private Integer toId;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态
     * 0-未读;1-已读;2-删除;
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
