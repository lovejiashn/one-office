package com.jiashn.oneofficesso.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jiashn.oneofficesso.user.domain.OpuAdmin;
import com.jiashn.oneofficesso.user.domain.req.UserLoginReq;
import com.jiashn.oneofficesso.user.mapper.UserManageMapper;
import com.jiashn.oneofficesso.user.service.UserManageService;
import com.jiashn.oneofficesso.utils.JsonResult;
import com.jiashn.oneofficesso.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${sso.is-open-captcha}")
    private Boolean isOpenCaptcha;

    @Override
    public JsonResult<?> userLogin(UserLoginReq loginReq, HttpSession session) {
        if (isOpenCaptcha){
            if (StringUtils.isBlank(loginReq.getCaptcha())){
                return JsonResult.fail(4040, "请输入验证码");
            }
            if (!loginReq.getCaptcha().equalsIgnoreCase(String.valueOf(session.getAttribute("captcha")))){
                return JsonResult.fail(5000, "验证码输入错误");
            }
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginReq.getUsername());
        if (Objects.isNull(userDetails) || !passwordEncoder.matches(loginReq.getPassword(),userDetails.getPassword())){
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
}
