package com.jiashn.oneofficesso.user.service;

import com.jiashn.oneofficesso.user.domain.OpuAdmin;

/**
 * @author jiangjs
 * @date 2022-05-19 22:30
 */
public interface OpuAdminService {

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 返回用户信息
     */
    OpuAdmin getUserInfoByUsername(String username);
}
