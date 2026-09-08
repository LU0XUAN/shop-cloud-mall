package com.shop.user.controller;

import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysUser;
import com.shop.core.utils.PageUtils;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/info/{identifier}")
    @Log(describe = "根据用户名获取用户信息")
    public R<?> getUser(@PathVariable String identifier) {
        SysUser user = userService.lambdaQuery().eq(SysUser::getUsername, identifier).one();
        return R.ok(user);
    }

    @GetMapping("/getInfo")
    @Log(describe = "获取用户信息")
    public R<?> getInfo() {
        Long userId = SecurityUtils.getUserId();
        SysUser user = userService.getById(userId);
        return R.ok(user);
    }

    @PostMapping
    @Log(describe = "新增用户")
    public R<?> add(@RequestBody SysUser item) {
        String username = item.getUsername();
        if (userService.lambdaQuery().eq(SysUser::getUsername, username).count() > 0) {
            return R.fail("用户名已存在");
        }
        item.setPassword(SecurityUtils.encryptPassword(item.getPassword()));
        return R.ok(userService.save(item));
    }

    @PostMapping("/register")
    @Log(describe = "注册用户")
    public R<?> register(@RequestBody SysUser item) {
        String username = item.getUsername();
        if (userService.lambdaQuery().eq(SysUser::getUsername, username).count() > 0) {
            return R.fail("用户名已存在");
        }
        item.setPassword(SecurityUtils.encryptPassword(item.getPassword()));
        item.setRole(1L);
        return R.ok(userService.save(item));
    }

    @GetMapping("/list")
    @Log(describe = "查询用户列表")
    public R<?> list(SysUser item) {
        PageUtils.start();
        List<SysUser> list = userService.lambdaQuery()
                .like(item.getNickname() != null, SysUser::getNickname, item.getNickname())
                .like(item.getUsername() != null, SysUser::getUsername, item.getUsername())
                .list();
        return R.ok(list);
    }

    @PutMapping
    @Log(describe = "修改用户")
    public R<?> edit(@RequestBody SysUser item) {
        item.setPassword(null);
        return R.ok(userService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除用户")
    public R<?> delete(@PathVariable Long id) {
        return R.ok(userService.removeById(id));
    }

    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取用户信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(userService.getById(id));
    }

    @PutMapping("/pwd")
    @Log(describe = "修改密码")
    public R<?> editPwd(@RequestBody SysUser sysUser) {
        return R.ok(userService.editPwd(sysUser));
    }

    @PutMapping("/edit")
    @Log(describe = "修改用户个人信息")
    public R<?> editUserInfo(@RequestBody SysUser sysUser) {
        sysUser.setUserId(SecurityUtils.getUserId());
        sysUser.setNickname(sysUser.getNickname());
        sysUser.setSex(sysUser.getSex());
        sysUser.setAvatar(sysUser.getAvatar());
        return R.ok(userService.updateById(sysUser));
    }

    @PostMapping("/updateIntegral")
    @Log(describe = "修改用户积分信息")
    public R<?> updateIntegral(@RequestBody SysUser sysUser) {
        return R.ok(userService.updateById(sysUser));
    }
}
