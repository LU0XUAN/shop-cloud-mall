package com.shop.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysAddr;
import com.shop.user.mapper.SysAddrMapper;
import com.shop.user.service.SysAddrService;
import org.springframework.stereotype.Service;

@Service
public class SysAddrServiceImpl extends ServiceImpl<SysAddrMapper, SysAddr> implements SysAddrService {
}
