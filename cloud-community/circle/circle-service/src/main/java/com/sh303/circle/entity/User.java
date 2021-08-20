package com.sh303.circle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @program: cloud-community
 * @description: 用户
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 0-普通用户; 1-超级管理员; 2-版主;
     */
    private Integer type;

    /**
     * 状态
     * 0-未激活; 1-已激活;
     */
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
