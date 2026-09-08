package com.shop.security.service;

import cn.hutool.core.lang.UUID;
import com.shop.core.constant.Constants;
import com.shop.core.constant.SecurityConstants;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.JwtUtils;
import com.shop.core.utils.ServletUtils;
import com.shop.core.utils.StringUtils;
import com.shop.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/11 0:01
 * @Version: 1.0
 * @Description:
 */
public class TokenService {
    @Autowired
    private RedisService redisService;


    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = Constants.EXPIRATION;


    private final static Long MILLIS_MINUTE_TEN = Constants.REFRESH_TIME * MILLIS_MINUTE;
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(Constants.LOGIN_TOKEN_KEY + userkey);
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = JwtUtils.getUserKey(token);
            redisService.deleteObject(Constants.LOGIN_TOKEN_KEY + userKey);
        }
    }

    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = Constants.LOGIN_TOKEN_KEY + loginUser.getToken();
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = UUID.fastUUID().toString();
        Long userId = loginUser.getSysUser().getUserId();
        String identifier = loginUser.getUsername();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(identifier);
        //刷新token
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_IDENTIFIER, identifier);

        // 接口返回信息
        Map<String, Object> rspMap = new LinkedHashMap<>();
        rspMap.put("expires_in", expireTime);
        rspMap.put("userInfo", loginUser.getSysUser());
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        return rspMap;
    }

}
