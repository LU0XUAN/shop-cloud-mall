package com.shop.auth.service;

import com.shop.core.constant.Constants;
import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysUser;
import com.shop.core.domain.model.LoginUser;
import com.shop.core.utils.StringUtils;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.user.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysLoginService {

    @Autowired
    private ApiUserService userService;

    public LoginUser login(String username, String password) {
        R<SysUser> userResult = userService.getUserInfo(username);
        if (Constants.FAIL.equals(userResult.getCode())) {
            throw new RuntimeException(userResult.getMsg());
        }
        SysUser userInfo = userResult.getData();
        if (StringUtils.isNull(userInfo)) {
            throw new RuntimeException("登录用户: " + username + " 不存在");
        }
        if (Constants.ONE.equals(userInfo.getStatus())) {
            throw new RuntimeException("对不起，您的账号: " + username + " 已停用");
        }
        if (!SecurityUtils.matchesPassword(password, userInfo.getPassword())) {
            throw new RuntimeException("用户不存在/密码错误");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(userInfo);
        loginUser.setUsername(userInfo.getUsername());
        loginUser.setUserid(userInfo.getUserId());
        return loginUser;
    }

    public void register(LoginUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String nickName = user.getNickname();
        String avatar = user.getAvatar();
        R<SysUser> userResult = userService.getUserInfo(username);
        SysUser userInfo = userResult.getData();
        if (userInfo != null) {
            throw new RuntimeException("登录用户: " + username + " 已存在");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        sysUser.setNickname(nickName);
        sysUser.setAvatar(avatar);
        userService.registerUserInfo(sysUser);
    }
}