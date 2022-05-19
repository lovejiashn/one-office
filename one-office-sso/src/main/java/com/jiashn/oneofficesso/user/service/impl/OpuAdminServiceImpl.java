package com.jiashn.oneofficesso.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jiashn.oneofficesso.user.domain.OpuAdmin;
import com.jiashn.oneofficesso.user.mapper.UserManageMapper;
import com.jiashn.oneofficesso.user.service.OpuAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangjs
 * @date 2022-05-19 22:30
 */
@Service
public class OpuAdminServiceImpl implements OpuAdminService {
    @Autowired
    private UserManageMapper userManageMapper;

    @Override
    public OpuAdmin getUserInfoByUsername(String username) {
        Wrapper<OpuAdmin> adminWrapper = Wrappers.<OpuAdmin>lambdaQuery().eq(OpuAdmin::getName,username)
                .eq(OpuAdmin::getEnabled,Boolean.TRUE);
        return userManageMapper.selectOne(adminWrapper);
    }
}
