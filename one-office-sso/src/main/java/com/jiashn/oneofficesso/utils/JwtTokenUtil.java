package com.jiashn.oneofficesso.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * jwt工具类
 * @author jiangjs
 * @date 2022-05-17 6:47
 */
@Component
public class JwtTokenUtil {

    /**
     * 负荷用户名称的key
     */
    private static final String CLAIM_KEY_USERNAME = "sub";
    /**
     * 负荷的创建者的key
     */
    private static final String CLAIM_KEY_CREATED_TIME = "created";

    /**
     * 加密使用的秘钥
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 过期时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成用户的token
     * @param userDetails 用户信息
     * @return 生成的token
     */
    public String createUserToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED_TIME,new Date());
        return this.generateToken(claims);
    }

    /**
     * 通过token获取用户名
     * @param token 用户token
     * @return 返回用户名
     */
    public String getUserNameByToken(String token){
        //获取负荷
        Claims claims = this.getClaimsByToken(token);
        return Objects.nonNull(claims) ? claims.getSubject() : null;
    }

    /**
     * 验证token的有效性，1、验证用户是否一致  2、验证token是否有效
     * @param token 用户token
     * @return 返回验证结果
     */
    public boolean validateToken(String token,UserDetails userDetails){
        //获取用户
        String username = getUserNameByToken(token);
        //验证token是否有效
        return StringUtils.isNotBlank(username) && Objects.equals(username,userDetails.getUsername()) && isValidateToken(token);
    }

    /**
     * 是否能刷新token
     * @param token 用户token
     * @return 判断结果
     */
    public boolean canRefresh(String token){
        return !isValidateToken(token);
    }

    /**
     * 刷新token，即重新设置负荷的时间
     * @param token 原来token
     * @return 返回刷新后的token
     */
    public String refreshToken(String token){
        Claims claims = getClaimsByToken(token);
        claims.put(CLAIM_KEY_CREATED_TIME,new Date());
        return this.generateToken(claims);
    }

    private boolean isValidateToken(String token){
        //通过获取有效时间来判断是否过期
        Date expirationDate = getExpirationDate(token);
        return Objects.nonNull(expirationDate) ? new Date().before(expirationDate) : Boolean.FALSE;

    }

    private Date getExpirationDate(String token){
        Claims claims = this.getClaimsByToken(token);
        return Objects.nonNull(claims) ? claims.getExpiration() : null;
    }

    /**
     * 从token中获取负荷
     * @param token token
     * @return 返回负荷
     */
    private Claims getClaimsByToken(String token){
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            claims = e.getClaims();
        }
        return claims;
    }

    /**
     * 生成token
     * @param claims 负荷
     * @return 返回token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                //设置负荷
                .setClaims(claims)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                //签名方式
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
