package com.shop.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.core.domain.entity.SysOrder;

import java.util.List;

public interface SysOrderService extends IService<SysOrder> {
    List<SysOrder> getMyOrder(SysOrder item);
}//承接 Controller 控制层，向下调用