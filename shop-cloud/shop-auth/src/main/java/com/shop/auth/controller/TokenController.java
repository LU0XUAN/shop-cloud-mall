package com.shop.auth.controller;

import com.shop.core.domain.R;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.StringUtils;
import com.shop.auth.service.SysLoginService;
import com.shop.security.service.TokenService;
import com.shop.security.utils.AuthUtils;
import com.shop.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 用户登录
     * @param form
     * @return
     */
    @PostMapping("login")
    public R<?> login(@RequestBody LoginUser form) {
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        return R.ok(tokenService.createToken(userInfo));
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            AuthUtils.logoutByToken(token);
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody LoginUser user) {
        sysLoginService.register(user);
        return R.ok();
    }
}