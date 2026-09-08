package com.shop.security.interceptor;

import com.shop.core.constant.SecurityConstants;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.ServletUtils;
import com.shop.core.utils.StringUtils;
import com.shop.core.utils.ThreadLocalUtils;
import com.shop.security.utils.AuthUtils;
import com.shop.security.utils.SecurityUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 23:45
 * @Version: 1.0
 * @Description:
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String userId = ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID);
        String identifier = ServletUtils.getHeader(request, SecurityConstants.DETAILS_IDENTIFIER);
        String userKey = ServletUtils.getHeader(request, SecurityConstants.USER_KEY);
        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtils.getLoginUser(token);
            if (loginUser != null) {
                // 如果请求头中的参数为空，则使用登录用户的信息填充
                ThreadLocalUtils.setUserId(StringUtils.isNotEmpty(userId) ? userId : loginUser.getUserid().toString());
                ThreadLocalUtils.setUserName(identifier);
                ThreadLocalUtils.setUserKey(StringUtils.isNotEmpty(userKey) ? userKey : loginUser.getToken());

                AuthUtils.verifyLoginUserExpire(loginUser);
                ThreadLocalUtils.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }

}
