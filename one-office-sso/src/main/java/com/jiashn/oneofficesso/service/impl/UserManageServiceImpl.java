package com.jiashn.oneofficesso.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jiashn.oneofficesso.domain.OpuAdmin;
import com.jiashn.oneofficesso.domain.req.UserLoginReq;
import com.jiashn.oneofficesso.mapper.UserManageMapper;
import com.jiashn.oneofficesso.service.UserManageService;
import com.jiashn.oneofficesso.utils.JsonResult;
import com.jiashn.oneofficesso.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author jiangjs
 * @date 2022-05-18 20:43
 */
@Service
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserManageMapper userManageMapper;

    @Value("${jwt.tokenHead")
    private String tokenHead;

    @Override
    public JsonResult<?> userLogin(UserLoginReq loginReq) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginReq.getUsername());
        if (Objects.isNull(userDetails) || !passwordEncoder.matches(userDetails.getPassword(),loginReq.getPassword())){
            return JsonResult.fail(4040, "用户名或密码不正确");
        }
        if (!userDetails.isEnabled()){
            return JsonResult.fail(4040, "账号已被禁用,请联系管理员");
        }
        //更新security登录用户对象,便于获取用户信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //调用方法生成token
        String userToken = jwtTokenUtil.createUserToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>(2);
        tokenMap.put("token",userToken);
        tokenMap.put("tokenHead",tokenHead);
        return JsonResult.success("登录成功",tokenMap);
    }

    @Override
    public JsonResult<?> getUserInfo(Principal principal) {
        if (Objects.isNull(principal)){
            return JsonResult.fail("当前用户未登录");
        }
        String username = principal.getName();
        //根据用户名获取用户信息
        Wrapper<OpuAdmin> adminWrapper = Wrappers.<OpuAdmin>lambdaQuery().eq(OpuAdmin::getName,username)
                .eq(OpuAdmin::getEnabled,Boolean.TRUE);
        OpuAdmin opuAdmin = userManageMapper.selectOne(adminWrapper);
        opuAdmin.setPassword(null);
        return JsonResult.success(opuAdmin);
    }

    @Override
    public OpuAdmin getUserInfoByUsername(String username) {
        Wrapper<OpuAdmin> adminWrapper = Wrappers.<OpuAdmin>lambdaQuery().eq(OpuAdmin::getName,username)
                .eq(OpuAdmin::getEnabled,Boolean.TRUE);
        return userManageMapper.selectOne(adminWrapper);
    }
}
