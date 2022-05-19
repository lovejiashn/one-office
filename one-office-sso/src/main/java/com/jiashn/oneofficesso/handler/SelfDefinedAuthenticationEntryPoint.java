package com.jiashn.oneofficesso.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiashn.oneofficesso.utils.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户未登录时，自定义返回结果
 * @author jiangjs
 * @date 2022-05-19 20:21
 */
@Component
public class SelfDefinedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        JsonResult<Object> fail = JsonResult.fail(401, "用户未登录，请登录");
        writer.write(new ObjectMapper().writeValueAsString(fail));
        writer.flush();
        writer.close();
    }
}
