package com.sh303.circle.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloud-community
 * @description: 用户DTO
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@ApiModel(value = "UserDTO", description = "")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 类型
     */
    @ApiModelProperty(value = "0-普通用户; 1-超级管理员; 2-版主;")
    private Integer type;

    /**
     * 状态
     */
    @ApiModelProperty(value = "0-未激活; 1-已激活;")
    private Integer status;

    /**
     * 激活码
     */
    private String activationCode;

    /**
     * 头像地址
     */
    private String headerUrl;

    /**
     * 创建时间
     */
    private Date createTime;

}
