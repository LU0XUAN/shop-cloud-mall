package com.shop.goods.controller;

import com.alibaba.fastjson2.JSON;
import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysCart;
import com.shop.core.domain.entity.SysGoods;
import com.shop.core.domain.vo.AttrVo;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.goods.service.SysCartService;
import com.shop.goods.service.SysGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private SysCartService cartService;

    @Autowired
    private SysGoodsService goodsService;

    @GetMapping("/getMyCart")
    @Log(describe = "获取我的购物车")
    public R<?> getMyCart() {
        List<SysCart> list = cartService.lambdaQuery()
                .eq(SysCart::getCreateBy, SecurityUtils.getUserId())
                .list();
        return R.ok(list);
    }

    @PostMapping
    @Log(describe = "添加购物车")
    public R<?> addCart(@RequestBody SysCart item) {
        // 根据商品ID查询商品信息
        SysGoods goods = goodsService.getById(item.getPid());
        if (goods == null) {
            return R.fail("商品不存在");
        }

        // 填充商品信息到购物车
        item.setCreateBy(SecurityUtils.getUserId());
        item.setName(goods.getName());
        item.setImg(goods.getImg());
        item.setImgs(goods.getImgs());
        item.setDetails(goods.getDetails());
        item.setType(goods.getType());
        item.setEnvironment(goods.getEnvironment());

        // 根据属性名称获取对应的属性信息和价格
        String attrName = item.getAttr();
        boolean attrMatched = false;
        if (attrName != null && goods.getAttr() != null) {
            List<AttrVo> attrVos = JSON.parseArray(goods.getAttr(), AttrVo.class);
            for (AttrVo attrVo : attrVos) {
                if (attrName.equals(attrVo.getName())) {
                    if (attrVo.getPrice() != null) {
                        item.setPrice(attrVo.getPrice().toPlainString());
                    }
                    item.setAttr(JSON.toJSONString(attrVo));
                    attrMatched = true;
                    break;
                }
            }
        }
        if (!attrMatched) {
            return R.fail("商品属性不存在");
        }

        // 默认数量为1
        if (item.getNum() == null) {
            item.setNum(1);
        }

        // 计算总价
        if (item.getPrice() != null && item.getNum() != null) {
            double price = Double.parseDouble(item.getPrice());
            double sum = price * item.getNum();
            item.setSum(String.valueOf(sum));
        }

        return R.ok(cartService.save(item));
    }

    @PutMapping("/editNum")
    @Log(describe = "修改购物车数量")
    public R<?> editNum(@RequestBody SysCart item) {
        SysCart cart = cartService.getById(item.getId());
        if (cart != null) {
            cart.setNum(item.getNum());
            // 重新计算总价
            if (cart.getPrice() != null && item.getNum() != null) {
                double price = Double.parseDouble(cart.getPrice());
                double sum = price * item.getNum();
                cart.setSum(String.valueOf(sum));
            }
            cartService.updateById(cart);
        }
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除购物车商品")
    public R<?> del(@PathVariable Long id) {
        return R.ok(cartService.removeById(id));
    }

    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取购物车信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(cartService.getById(id));
    }
}
