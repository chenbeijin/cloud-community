package com.sh303.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 响应通用参数包装
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "RestResponse<T>", description = "响应通用参数包装")
public class RestResponse<T> {

    @ApiModelProperty("响应错误编码,0为正常")
    private int code;

    @ApiModelProperty("响应错误信息")
    private String msg;

    @ApiModelProperty("响应内容")
    private T result;

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<T>();
    }

    /**
     * 成功
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    /**
     * 成功
     */
    @JsonInclude
    public Boolean isSuccessful() {
        return this.code == 0;
    }

    /**
     * 有效的失败
     */
    public static <T> RestResponse<T> validFail(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-2);
        response.setMsg(msg);
        return response;
    }
}
