package com.shop.user.user;

import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysUser;
import com.shop.user.factory.ApiUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "apiUserService", value = "shop-user", name = "shop-user", fallbackFactory = ApiUserFallbackFactory.class)
public interface ApiUserService {

    @GetMapping("/user/info/{identifier}")
    R<SysUser> getUserInfo(@PathVariable("identifier") String identifier);

    @PostMapping("/user/register")
    R<?> registerUserInfo(@RequestBody SysUser sysUser);

    @GetMapping("/user/getById/{id}")
    R<SysUser> getUserById(@PathVariable("id") Long id);

    @PostMapping("/user/updateIntegral")
    R<?> updateIntegral(@RequestBody SysUser sysUser);
}
