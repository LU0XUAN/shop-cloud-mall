package com.shop.user.factory;

import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysUser;
import com.shop.user.user.ApiUserService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ApiUserFallbackFactory implements FallbackFactory<ApiUserService> {

    @Override
    public ApiUserService create(Throwable cause) {
        return new ApiUserService() {
            @Override
            public R<SysUser> getUserInfo(String identifier) {
                return R.fail("远程调用失败");
            }

            @Override
            public R<?> registerUserInfo(SysUser sysUser) {
                return null;
            }

            @Override
            public R<SysUser> getUserById(Long id) {
                return null;
            }

            @Override
            public R<?> updateIntegral(SysUser sysUser) {
                return null;
            }
        };
    }
}
