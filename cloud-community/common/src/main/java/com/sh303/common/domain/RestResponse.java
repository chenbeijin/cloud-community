package com.sh303.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: cloud-community
 * @description: 响应通用参数包装
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "RestResponse<T>", description = "响应通用参数包装")
public class RestResponse<T> implements Serializable {

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
     * create by: Chen Bei Jin
     * description: 成功
     * create time: 2021/8/16 14:07
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<T>();
    }

    /**
     * create by: Chen Bei Jin
     * description: 成功
     * create time: 2021/8/16 14:07
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    /**
     * create by: Chen Bei Jin
     * description: 成功
     * create time: 2021/8/16 14:07
     */
    @JsonInclude
    public Boolean isSuccessful() {
        return this.code == 0;
    }

    /**
     * create by: Chen Bei Jin
     * description: 有效的失败
     * create time: 2021/8/16 14:07
     */
    public static <T> RestResponse<T> validFail(String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.setCode(-2);
        response.setMsg(msg);
        return response;
    }
}
