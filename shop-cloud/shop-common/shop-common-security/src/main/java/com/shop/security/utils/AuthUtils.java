package com.shop.security.utils;

import com.shop.core.domain.model.LoginUser;
import com.shop.security.auth.AuthLogic;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 23:57
 * @Version: 1.0
 * @Description:
 */
public class AuthUtils {
    /**
     * 底层的 AuthLogic 对象
     */
    public static AuthLogic authLogic = new AuthLogic();

    public static void logoutByToken(String token) {
        authLogic.logoutByToken(token);
    }

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getLoginUser(String token) {
        return authLogic.getLoginUser(token);
    }

    public static void verifyLoginUserExpire(LoginUser loginUser) {
        authLogic.verifyLoginUserExpire(loginUser);
    }
}
