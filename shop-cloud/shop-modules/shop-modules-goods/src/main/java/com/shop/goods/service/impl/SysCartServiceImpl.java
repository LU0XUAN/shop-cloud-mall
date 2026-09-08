package com.shop.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysCart;
import com.shop.goods.mapper.SysCartMapper;
import com.shop.goods.service.SysCartService;
import org.springframework.stereotype.Service;

@Service
public class SysCartServiceImpl extends ServiceImpl<SysCartMapper, SysCart> implements SysCartService {
}
