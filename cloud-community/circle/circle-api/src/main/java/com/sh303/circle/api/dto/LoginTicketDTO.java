package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="LoginTicketDTO", description="")
public class LoginTicketDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private String ticket;

    @ApiModelProperty(value = "0-有效; 1-无效;")
    private Integer status;

    private LocalDateTime expired;


}
