package com.shop.core.result;

/**
 * @Author: shop-cloud
 * @Date: 2024/7/10 21:57
 * @Version 1.0
 * 统一返回制定码
 */
public interface CustomizeResultCode {

    /**
     * 获取状态码
     * */
    Integer getCode();

    /**
     * 获取信息
     * */
    String getMsg();
}
