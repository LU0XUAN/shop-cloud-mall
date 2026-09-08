package com.shop.security.feign;

import com.shop.core.constant.SecurityConstants;
import com.shop.core.utils.ServletUtils;
import com.shop.core.utils.StringUtils;
import com.shop.core.utils.IpUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/13 18:42
 * @Version: 1.0
 * @Description:
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String userId = headers.get(SecurityConstants.DETAILS_USER_ID);
            if (StringUtils.isNotEmpty(userId)) {
                requestTemplate.header(SecurityConstants.DETAILS_USER_ID, userId);
            }
            //相当于userName的凭证
            String identifier = headers.get(SecurityConstants.DETAILS_IDENTIFIER);
            if (StringUtils.isNotEmpty(identifier)) {
                requestTemplate.header(SecurityConstants.DETAILS_IDENTIFIER, identifier);
            }
            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
            if (StringUtils.isNotEmpty(authentication)) {
                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
        }
    }
}
