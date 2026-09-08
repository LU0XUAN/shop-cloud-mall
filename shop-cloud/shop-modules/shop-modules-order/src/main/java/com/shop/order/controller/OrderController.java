package com.shop.order.controller;

import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysCart;
import com.shop.core.domain.entity.SysOrder;
import com.shop.core.domain.vo.EchartsVo;
import com.shop.core.utils.PageUtils;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.order.mapper.SysCartMapper;
import com.shop.order.service.SysOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private SysOrderService orderService;


    @Autowired
    private SysCartMapper cartMapper;

    @GetMapping("/list")
    @Log(describe = "查询订单列表")
    public R<?> list(SysOrder item) {
        PageUtils.start();
        // 使用MyBatis Plus Lambda查询，类型安全
        List<SysOrder> list = orderService.lambdaQuery()
                .like(item.getName() != null, SysOrder::getName, item.getName())
                .like(item.getNumbers() != null, SysOrder::getNumbers, item.getNumbers())
                .eq(item.getStatus() != null, SysOrder::getStatus, item.getStatus())
                .orderByDesc(SysOrder::getCreateTime)
                .list();
        return R.ok(list);
    }

    @GetMapping("/getMyOrder")
    @Log(describe = "获取我的订单")
    public R<?> getMyOrder(SysOrder item) {
        // 调用Service层的自定义方法，自动注入当前用户ID
        List<SysOrder> list = orderService.getMyOrder(item);
        return R.ok(list);
    }

    @GetMapping("/productStats")
    @Log(describe = "商品销量统计")
    public R<?> productStats() {
        // 查询所有订单
        List<SysOrder> allOrders = orderService.lambdaQuery()
                .list();
        
        // 按商品名称分组统计销量
        Map<String, Integer> salesMap = new HashMap<>();
        for (SysOrder order : allOrders) {
            String name = order.getName();
            Integer num = order.getNum();
            if (name != null && num != null) {
                // 累加销量，不存在则默认为0
                salesMap.put(name, salesMap.getOrDefault(name, 0) + num);
            }
        }
        
        // 转换为EchartsVo格式，便于前端图表展示
        List<EchartsVo> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
            EchartsVo vo = new EchartsVo();
            vo.setName(entry.getKey());    // 商品名称
            vo.setValue(entry.getValue()); // 销量
            result.add(vo);
        }
        
        // 按销量降序排序，取前10名
        result.sort((a, b) -> b.getValue() - a.getValue());
        if (result.size() > 10) {
            result = result.subList(0, 10);
        }
        
        return R.ok(result);
    }


    @PostMapping
    @Log(describe = "新增订单")
    public R<?> add(@RequestBody List<Map<String, Object>> items,
                    @RequestParam(required = false) Boolean useIntegrals,
                    @RequestParam(required = false) Integer point) {
        // 获取当前登录用户ID（从JWT Token中解析）
        Long userId = SecurityUtils.getUserId();
        
        // 生成订单号前缀：DD-时间戳-
        String orderPrefix = "DD-" + System.currentTimeMillis() + "-";
        
        // 遍历购物车商品，批量创建订单
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = items.get(i);
            
            // 解析购物车ID和地址ID
            Integer cid = toInteger(item.get("cid"));
            Integer aid = toInteger(item.get("aid"));
            
            // 解析折扣金额和实付金额
            String discount = toPlainString(item.get("discount"));
            String finalPrice = toPlainString(item.get("finalPrice"));
            
            // 跳过无效的购物车ID
            if (cid == null) {
                continue;
            }
            
            // 根据购物车ID查询购物车信息
            SysCart cart = cartMapper.selectById(cid);
            if (cart == null) {
                continue;
            }
            
            // 创建订单对象，复制购物车商品信息
            SysOrder order = new SysOrder();
            order.setCreateBy(userId);          // 创建人ID
            order.setPid(cart.getPid());        // 商品ID
            order.setAid(aid);                  // 收货地址ID
            order.setName(cart.getName());      // 商品名称
            order.setPrice(cart.getPrice());    // 单价
            order.setSum(cart.getSum());        // 原价总和
            order.setDiscount(discount);        // 折扣金额（积分抵扣）
            order.setFinalPrice(finalPrice);    // 实付金额
            order.setImg(cart.getImg());        // 封面图
            order.setImgs(cart.getImgs());      // 轮播图
            order.setDetails(cart.getDetails());// 商品详情
            order.setNum(cart.getNum());        // 购买数量
            order.setAttr(cart.getAttr());      // 商品属性（JSON）
            order.setType(cart.getType());      // 商品类型
            order.setEnvironment(cart.getEnvironment()); // 环保程度
            
            // 生成唯一订单号：DD-时间戳-序号
            String numbers = orderPrefix + (i + 1);
            order.setNumbers(numbers);
            
            // 设置订单状态：0表示已下单
            order.setStatus("0");
            
            // 计算积分：消费1元得1积分，向下取整
            if (finalPrice != null) {
                BigDecimal price = new BigDecimal(finalPrice);
                order.setIntegral(String.valueOf(price.setScale(0, RoundingMode.DOWN).intValue()));
            }
            
            // 保存订单到数据库
            orderService.save(order);
        }
        
        return R.ok();
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        String text = String.valueOf(value);
        if (text.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(text).intValue();
    }


    private String toPlainString(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value);
        if (text.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(text).toPlainString();
    }


    @PutMapping
    @Log(describe = "修改订单")
    public R<?> edit(@RequestBody SysOrder item) {
        return R.ok(orderService.updateById(item));
    }


    @PutMapping("/editStatus")
    @Log(describe = "修改订单状态")
    public R<?> editStatus(@RequestBody SysOrder item) {
        // 仅更新ID和状态，避免其他字段被意外修改
        SysOrder order = new SysOrder();
        order.setId(item.getId());
        order.setStatus(item.getStatus());
        return R.ok(orderService.updateById(order));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除订单")
    public R<?> delete(@PathVariable Long id) {
        return R.ok(orderService.removeById(id));
    }


    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取订单信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(orderService.getById(id));
    }
}
