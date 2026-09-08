package com.shop.api.order.service;

import com.shop.core.domain.entity.SysOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shop-order")
public interface OrderService {

    @GetMapping("/order/getById/{id}")
    SysOrder getById(@PathVariable("id") Integer id);
}
