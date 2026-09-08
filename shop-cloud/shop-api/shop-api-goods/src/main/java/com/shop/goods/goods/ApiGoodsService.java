package com.shop.goods.goods;

import com.shop.core.domain.R;
import com.shop.goods.factory.ApiGoodsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "apiGoodsService", value = "shop-goods", name = "shop-goods", fallbackFactory = ApiGoodsFallbackFactory.class)
public interface ApiGoodsService {

    @GetMapping("/goods/editAddrNum/{id}/{name}/{num}")
    R<?> editAddrNum(@PathVariable("id") String id, @PathVariable("name") String name, @PathVariable("num") Integer num);
}