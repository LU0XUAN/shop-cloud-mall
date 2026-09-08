package com.shop.core.constant;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 22:54
 * @Version: 1.0
 * @Description:
 */
public interface SecurityConstants {
    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "user_id";


    String DETAILS_IDENTIFIER = "username";

    /**
     * 授权信息字段
     */
    String AUTHORIZATION_HEADER = "authorization";

    /**
     * 请求来源
     */
    String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    String INNER = "inner";

    /**
     * 用户标识
     */
    String USER_KEY = "user_key";

    /**
     * 登录用户
     */
    String LOGIN_USER = "login_user";

    /**
     * 角色权限
     */
    String ROLE_PERMISSION = "role_permission";
}
