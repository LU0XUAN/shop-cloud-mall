package com.shop.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.core.domain.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    int editPwd(SysUser sysUser);
}
