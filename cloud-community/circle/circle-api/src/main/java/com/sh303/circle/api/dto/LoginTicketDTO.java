package com.sh303.circle.api.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloud-community
 * @description: 登录记录DTO
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@ApiModel(value = "LoginTicketDTO", description = "")
public class LoginTicketDTO implements Serializable {

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
     * 记录
     */
    private String ticket;

    /**
     * 状态
     */
    @ApiModelProperty(value = "0-有效; 1-无效;")
    private Integer status;

    /**
     * 过期时间
     */
    private Date expired;

}
