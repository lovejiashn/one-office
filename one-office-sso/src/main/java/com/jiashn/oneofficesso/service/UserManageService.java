package com.jiashn.oneofficesso.service;

import com.jiashn.oneofficesso.domain.OpuAdmin;
import com.jiashn.oneofficesso.domain.req.UserLoginReq;
import com.jiashn.oneofficesso.utils.JsonResult;

import java.security.Principal;

/**
 * @author jiangjs
 * @date 2022-05-18 20:43
 */
public interface UserManageService {
    /**
     * 登录并获取token
     * @param loginReq 登录提交参数
     * @return 返回数据
     */
    JsonResult<?> userLogin(UserLoginReq loginReq);

    /**
     * 获取当前登录用户信息
     * @param principal 登录用户对象
     * @return 查询结果
     */
    JsonResult<?> getUserInfo(Principal principal);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    OpuAdmin getUserInfoByUsername(String username);

}
