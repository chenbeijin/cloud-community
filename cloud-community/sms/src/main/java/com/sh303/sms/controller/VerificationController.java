package com.sh303.sms.controller;

import com.sh303.common.domain.RestResponse;
import com.sh303.sms.dto.VerificationInfo;
import com.sh303.sms.service.VerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 验证码控制器
 */

@RestController
@Api(value = "验证码服务接口", tags = "验证码服务接口", description = "验证码服务接口")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    /**
     * 生成验证信息
     * @param name          业务名称
     * @param payload       业务携带参数，如手机号 ，邮箱
     * @param effectiveTime 验证信息有效期(秒)
     * @return
     */
    @ApiOperation(value = "生成验证信息", notes = "生成验证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "业务名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payload", value = "业务携带参数，如手机号 ，邮箱", required = true, paramType = "body"),
            @ApiImplicitParam(name = "effectiveTime", value = "验证信息有效期(秒)", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/generate")
    public RestResponse<VerificationInfo> generateVerificationInfo(@RequestParam(value = "name") String name,
                                                                   @RequestBody Map<String, Object> payload,
                                                                   @RequestParam("effectiveTime") int effectiveTime) {
        VerificationInfo verification = verificationService.generateVerificationInfo(name, payload, effectiveTime);
        return RestResponse.success(verification);
    }

    /**
     * 验证码校验
     * @param name             业务名称
     * @param verificationKey  验证码Key
     * @param verificationCode 验证码
     * @return
     */
    @ApiOperation(value = "校验", notes = "校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "业务名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verificationKey", value = "验证码Key", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verificationCode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/verify")
    public RestResponse<Boolean> verify(String name, String verificationKey, String verificationCode) {
        boolean isSuccess = verificationService.verify(name, verificationKey, verificationCode);
        // 返回成功信息
        return RestResponse.success(isSuccess);
    }

}
