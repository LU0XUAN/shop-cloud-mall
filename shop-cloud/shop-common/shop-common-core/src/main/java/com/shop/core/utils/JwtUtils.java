package com.shop.core.utils;

import cn.hutool.core.convert.Convert;
import com.shop.core.constant.Constants;
import com.shop.core.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 23:11
 * @Version: 1.0
 * @Description:
 */
public class JwtUtils {
    public static String secret = Constants.SECRET;


    public static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }


    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    public static String getIdentifier(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_IDENTIFIER);
    }

    public static String getIdentifier(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_IDENTIFIER);
    }

    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }
}
