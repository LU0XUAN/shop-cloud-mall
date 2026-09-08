package com.shop.core.constant;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 22:50
 * @Version: 1.0
 * @Description:
 */
public interface Constants {

    long EXPIRATION = 21600;
    long REFRESH_TIME = 120;

    Integer SUCCESS = 200;
    Integer FAIL = 500;

    //停用
    String ONE = "1";

    String UTF8 = "UTF-8";

    /**
     * 令牌自定义标识
     */
    String AUTHENTICATION = "Authorization";

    /**
     * 令牌前缀
     */
    String PREFIX = "Bearer ";

    /**
     * 令牌秘钥
     */
    String SECRET = "qwerty123!@!@%$^#%$sdfSDFJS";

    /**
     * redis存储幂等所生成token数据
     */
    String IDEMPOTENT = "idempotent:";

    String LOGIN_TOKEN_KEY = "tokens:";

    String RESOURCE_PREFIX = "/profile";
}
