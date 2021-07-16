package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="CommentDTO", description="")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;

    private Integer status;

    private LocalDateTime createTime;


}
