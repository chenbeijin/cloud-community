package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="UserDTO", description="")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String email;

    @ApiModelProperty(value = "0-普通用户; 1-超级管理员; 2-版主;")
    private Integer type;

    @ApiModelProperty(value = "0-未激活; 1-已激活;")
    private Integer status;

    private String activationCode;

    private String headerUrl;

    private LocalDateTime createTime;


}
