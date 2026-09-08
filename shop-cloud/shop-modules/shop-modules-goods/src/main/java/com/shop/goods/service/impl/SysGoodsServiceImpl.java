package com.shop.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysGoods;
import com.shop.goods.mapper.SysGoodsMapper;
import com.shop.goods.service.SysGoodsService;
import org.springframework.stereotype.Service;

@Service
public class SysGoodsServiceImpl extends ServiceImpl<SysGoodsMapper, SysGoods> implements SysGoodsService {
}