package com.cqz.beverage.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cqz.beverage.constant.JwtConstant.*;

/**
 * jwt工具类，生成，验证，解析token
 */
@Component
public class JwtTokenUtil {

    /**
     * 生成token
     * @param userId
     * @param username
     * @return
     */
    public String generateToken(Long userId,String username){
        Map<String,Object> payload = new HashMap<>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT,new Date());
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT,new Date(System.currentTimeMillis()+EXPIRATION));
        //自定义字段
        payload.put("userId", userId);
        payload.put("username", username);
        return JWTUtil.createToken(payload, SECRET.getBytes());
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public boolean validateToken(String token){
        try{
            JWT jwt = JWTUtil.parseToken(token).setKey(SECRET.getBytes());
            //验证签名+验证过期时间
            return jwt.verify() && jwt.validate(EXPIRATION);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 从token中获取用户id
     * @param token
     * @return
     */
    public Object getUserIdFromToken(String token){
        JWT jwt = JWTUtil.parseToken(token).setKey(SECRET.getBytes());
        Object userId = jwt.getPayload("userId");
        return userId;
    }

    /**
     * 从token中获取用户名称
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        JWT jwt = JWTUtil.parseToken(token).setKey(SECRET.getBytes());
        return (String)jwt.getPayload("username");
    }

    /**
     * 获取请求头中的token
      * @param headerValue
     * @return
     */
    public String getTokenFromHeader(String headerValue){
        if(headerValue==null||!headerValue.startsWith(TOKEN_PREFIX)){
            return null;
        }
        return headerValue.substring(TOKEN_PREFIX.length()).trim();
    }

    /**
     * getter方法
     * @return
     */
    public String getHeader(){
        return HEADER;
    }
}
