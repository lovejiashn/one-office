package com.jiashn.oneofficesso.user.controller;

import com.jiashn.oneofficesso.user.service.CaptchaService;
import com.jiashn.oneofficesso.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jiangjs
 * @date 2022-05-23 20:29
 */
@RestController
@RequestMapping("/captcha")
@Api(value = "验证码管理",tags = "验证码管理")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @ApiOperation(value = "获取验证码信息")
    @GetMapping("getCaptchaCode.do")
    public void getCaptchaCode(HttpServletResponse response, HttpSession session){
        captchaService.getCaptchaCode(response, session);
    }
}
