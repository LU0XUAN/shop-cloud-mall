package com.shop.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysType;
import com.shop.goods.mapper.SysTypeMapper;
import com.shop.goods.service.SysTypeService;
import org.springframework.stereotype.Service;

@Service
public class SysTypeServiceImpl extends ServiceImpl<SysTypeMapper, SysType> implements SysTypeService {
}