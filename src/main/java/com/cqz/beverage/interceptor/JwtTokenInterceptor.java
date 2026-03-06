package com.cqz.beverage.interceptor;

import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Jwt拦截器
 */
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerValue = request.getHeader(jwtTokenUtil.getHeader());
        String token = jwtTokenUtil.getTokenFromHeader(headerValue);
        //校验token是否为空
        if (token == null) {
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_LOGIN);
        }
        //验证token有效性
        if(!jwtTokenUtil.validateToken(token)){
            throw new BusinessException(BusinessExceptionEnum.USER_LOGIN_INVALID);
        }
        Object userId = jwtTokenUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        return true;
    }
}
