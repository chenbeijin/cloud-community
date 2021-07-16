package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="MessageDTO", description="")
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String conversationId;

    private String content;

    @ApiModelProperty(value = "0-未读;1-已读;2-删除;")
    private Integer status;

    private LocalDateTime createTime;


}
