package com.jiashn.oneofficesso.user.service;

import com.jiashn.oneofficesso.utils.JsonResult;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jiangjs
 * @date 2022-05-23 20:43
 */
public interface CaptchaService {

    /**
     * 获取验证码
     * @param response 响应
     * @param session 保存到session
     * @return 返回验证码
     */
    void getCaptchaCode( HttpServletResponse response, HttpSession session);
}
