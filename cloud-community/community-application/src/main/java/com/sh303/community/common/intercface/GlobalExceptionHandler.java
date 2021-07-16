package com.sh303.community.common.intercface;

import com.sh303.common.domain.BusinessException;
import com.sh303.common.domain.CommonErrorCode;
import com.sh303.common.domain.RestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */

@ControllerAdvice   //与@Exceptionhandler配合使用实现全局异常处理
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 进程异常时 给前端页面发送 错误详情
     * 注释： @ControllerAdvice 和 @ExceptionHandler 配合使用实现全局异常处理
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse processException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Exception e) {
        // 解析异常信息
        // 如果是系统自定义异常，直接取出errCode与 errMessage
        if (e instanceof BusinessException) {
            LOGGER.info(e.getMessage(), e);
            // 解析系统自定义异常信息
            BusinessException businessException = (BusinessException) e;
            // 返回自定义错误信息
            return new RestErrorResponse(String.valueOf(businessException.getErrorCode()), businessException.getMessage());
        }

        LOGGER.error("系统异常", e);
        // 统一定义为999999系统未知错误
        return new RestErrorResponse(String.valueOf(CommonErrorCode.UNKNOWN.getCode()), CommonErrorCode.UNKNOWN.getDesc());
    }

}
