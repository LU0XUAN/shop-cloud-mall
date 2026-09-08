package com.shop.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.core.domain.entity.SysOrder;
import com.shop.core.utils.PageUtils;
import com.shop.order.mapper.SysOrderMapper;
import com.shop.order.service.SysOrderService;
import com.shop.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder> implements SysOrderService {

    @Override
    public List<SysOrder> getMyOrder(SysOrder item) {
        PageUtils.start();
        return this.lambdaQuery()
                .eq(SysOrder::getCreateBy, SecurityUtils.getUserId())
                .like(item.getName() != null, SysOrder::getName, item.getName())
                .eq(item.getStatus() != null, SysOrder::getStatus, item.getStatus())
                .orderByDesc(SysOrder::getCreateTime)
                .list();
    }
}
