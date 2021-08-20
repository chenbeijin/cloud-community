package com.sh303.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: 错误响应参数包装
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@ApiModel(value = "RestErrorResponse", description = "错误响应参数包装")
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse implements Serializable {

    private String errCode;

    private String errMessage;

}
