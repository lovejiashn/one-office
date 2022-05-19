package com.jiashn.oneofficesso.controller;

import com.jiashn.oneofficesso.domain.req.UserLoginReq;
import com.jiashn.oneofficesso.service.UserManageService;
import com.jiashn.oneofficesso.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author jiangjs
 * @date 2022-05-18 7:30
 */
@RestController
@RequestMapping("/user")
public class UserManageController {

    @Autowired
    private UserManageService loginService;

    @PostMapping("/login.do")
    public JsonResult<?> userLogin(@RequestBody @Valid UserLoginReq loginReq){
        return loginService.userLogin(loginReq);
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
