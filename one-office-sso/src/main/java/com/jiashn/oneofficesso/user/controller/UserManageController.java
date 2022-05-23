package com.jiashn.oneofficesso.user.controller;

import com.jiashn.oneofficesso.user.domain.req.UserLoginReq;
import com.jiashn.oneofficesso.user.service.UserManageService;
import com.jiashn.oneofficesso.utils.JsonResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

/**
 * @author jiangjs
 * @date 2022-05-18 7:30
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户登录管理",tags = "用户登录管理")
public class UserManageController {

    @Autowired
    private UserManageService loginService;

    @PostMapping("/login.do")
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginReq", required = true, dataType = "UserLoginReq"),
    })
    public JsonResult<?> userLogin(@RequestBody @Valid UserLoginReq loginReq, HttpSession session){
        return loginService.userLogin(loginReq,session);
    }

    /**
     * 前后端分离时，前端调用退出方法，直接删除请求头的token
     * @return 操作结果
     */
    @GetMapping("/logOut.do")
    public JsonResult<?> logOut(){
        return JsonResult.success("退出成功");
    }

    @GetMapping("/getUserInfo.do")
    public JsonResult<?> getUserInfo(Principal principal){
        return loginService.getUserInfo(principal);
    }
}
