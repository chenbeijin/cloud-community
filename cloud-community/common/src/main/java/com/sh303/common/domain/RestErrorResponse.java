package com.sh303.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错误响应参数包装
 */

@Data
@ApiModel(value = "RestErrorResponse", description = "错误响应参数包装")
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse {

    private String errCode;

    private String errMessage;

}
