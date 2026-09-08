package com.shop.security.auth;

import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.SpringUtils;
import com.shop.security.service.TokenService;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/11 0:00
 * @Version: 1.0
 * @Description:
 */
public class AuthLogic {
    public TokenService tokenService = SpringUtils.getBean(TokenService.class);

    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }

    public void logoutByToken(String token) {
        tokenService.delLoginUser(token);
    }

    public void verifyLoginUserExpire(LoginUser loginUser) {
        tokenService.verifyToken(loginUser);
    }
}
