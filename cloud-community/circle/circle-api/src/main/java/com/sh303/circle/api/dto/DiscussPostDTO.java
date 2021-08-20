package com.sh303.circle.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloud-community
 * @description: 个人帖子DTO
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@ApiModel(value = "DiscussPostDTO", description = "")
public class DiscussPostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

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
     */
    @ApiModelProperty(value = "0-普通; 1-置顶;")
    private Integer type;

    /**
     * 状态
     */
    @ApiModelProperty(value = "0-正常; 1-精华; 2-拉黑;")
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
