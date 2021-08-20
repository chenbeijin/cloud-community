package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloud-community
 * @description: 信息DTO
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@ApiModel(value = "MessageDTO", description = "")
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     */
    @ApiModelProperty(value = "0-未读;1-已读;2-删除;")
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
