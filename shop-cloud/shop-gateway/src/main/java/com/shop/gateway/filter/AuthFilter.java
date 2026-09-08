package com.shop.gateway.filter;

import com.shop.core.constant.Constants;
import com.shop.core.constant.SecurityConstants;
import com.shop.core.utils.JwtUtils;
import com.shop.core.utils.ServletUtils;
import com.shop.core.utils.StringUtils;
import com.shop.gateway.config.IgnoreWhiteProperties;
import com.shop.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        org.springframework.http.server.reactive.ServerHttpRequest request = exchange.getRequest();
        org.springframework.http.server.reactive.ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();

        List<String> whites = ignoreWhite.getWhites();
        if (StringUtils.matches(url, whites)) {
            return chain.filter(exchange);
        }

        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }

        String userkey = JwtUtils.getUserKey(claims);
        boolean isLogin = redisService.hasKey(getTokenKey(userkey));
        if (!isLogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }

        String userId = JwtUtils.getUserId(claims);
        String identifier = JwtUtils.getIdentifier(claims);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(identifier)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userId);
        addHeader(mutate, SecurityConstants.DETAILS_IDENTIFIER, identifier);

        removeHeader(mutate, SecurityConstants.FROM_SOURCE);

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(org.springframework.http.server.reactive.ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(org.springframework.http.server.reactive.ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
    }

    private String getTokenKey(String token) {
        return Constants.LOGIN_TOKEN_KEY + token;
    }

    private String getToken(org.springframework.http.server.reactive.ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(Constants.AUTHENTICATION);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.PREFIX)) {
            token = token.replaceFirst(Constants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}