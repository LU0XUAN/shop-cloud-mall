package com.shop.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysUser;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.mapper.SysUserMapper;
import com.shop.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int editPwd(SysUser sysUser) {
        String encodePassword = SecurityUtils.encryptPassword(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        LambdaUpdateWrapper<SysUser> update = new LambdaUpdateWrapper<>();
        update.set(SysUser::getPassword, encodePassword);
        update.eq(SysUser::getUserId, SecurityUtils.getUserId());
        return sysUserMapper.update(null, update);
    }
}
