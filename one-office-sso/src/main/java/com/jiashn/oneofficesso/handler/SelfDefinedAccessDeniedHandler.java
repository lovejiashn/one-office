package com.jiashn.oneofficesso.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiashn.oneofficesso.utils.JsonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当访问接口没有权限时,自定义返回值
 * @author jiangjs
 * @date 2022-05-19 20:13
 */
@Component
public class SelfDefinedAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        JsonResult<Object> fail = JsonResult.fail(403, "权限不足，请联系管理员");
        writer.write(new ObjectMapper().writeValueAsString(fail));
        writer.flush();
        writer.close();
    }
}
