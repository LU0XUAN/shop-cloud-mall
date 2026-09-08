package com.shop.security.utils;

import com.shop.core.constant.Constants;
import com.shop.core.constant.SecurityConstants;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.ServletUtils;
import com.shop.core.utils.StringUtils;
import com.shop.core.utils.ThreadLocalUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 23:53
 * @Version: 1.0
 * @Description:
 */
public class SecurityUtils {
    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }
    public static String replaceTokenPrefix(String token) {
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.PREFIX)) {
            token = token.replaceFirst(Constants.PREFIX, "");
        }
        return token;
    }
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return ThreadLocalUtils.getUserId();
    }

    public static LoginUser getLoginUser() {
        return ThreadLocalUtils.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    public static Long getUserRole() {
        LoginUser loginUser = ThreadLocalUtils.get(SecurityConstants.LOGIN_USER, LoginUser.class);
        return loginUser.getSysUser().getRole();
    }

    /**
     *
     * @param rawPassword 存的密码
     * @param encodedPassword 传的密码
     * @return
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public static void main(String[] args) {
        String s = encryptPassword("123456");
        System.out.println(s);
        boolean b = matchesPassword("123456", s);
        System.out.println(b);
    }
}
