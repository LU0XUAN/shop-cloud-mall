package com.shop.api.user.service;

import com.shop.core.domain.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shop-user")
public interface UserService {

    @GetMapping("/user/getById/{userId}")
    SysUser getById(@PathVariable("userId") Long userId);
}
