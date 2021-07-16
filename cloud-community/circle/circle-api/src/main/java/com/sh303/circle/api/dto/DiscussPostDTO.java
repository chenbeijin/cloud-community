package com.sh303.circle.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="DiscussPostDTO", description="")
public class DiscussPostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userId;

    private String title;

    private String content;

    @ApiModelProperty(value = "0-普通; 1-置顶;")
    private Integer type;

    @ApiModelProperty(value = "0-正常; 1-精华; 2-拉黑;")
    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private Double score;


}
