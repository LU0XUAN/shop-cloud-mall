package com.shop.goods.factory;

import com.shop.core.domain.R;
import com.shop.goods.goods.ApiGoodsService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ApiGoodsFallbackFactory implements FallbackFactory<ApiGoodsService> {
    @Override
    public ApiGoodsService create(Throwable cause) {
        return new ApiGoodsService() {
            @Override
            public R<?> editAddrNum(String id, String name, Integer num) {
                return R.fail("远程调用商品服务失败");
            }
        };
    }
}