package com.shop.goods.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysGoods;
import com.shop.core.domain.entity.SysType;
import com.shop.core.domain.vo.AttrVo;
import com.shop.core.utils.PageUtils;
import com.shop.goods.service.SysGoodsService;
import com.shop.goods.service.SysTypeService;
import com.shop.security.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private SysGoodsService goodsService;

    @Autowired
    private SysTypeService typeService;

    @GetMapping("/list")
    @Log(describe = "查询商品列表")
    public R<?> list(SysGoods item) {
        PageUtils.start();
        List<SysGoods> list = goodsService.lambdaQuery()
                .like(item.getName() != null, SysGoods::getName, item.getName())
                .list();
        return R.ok(list);
    }

    @PostMapping
    @Log(describe = "新增商品")
    public R<?> add(@RequestBody SysGoods item) {
        return R.ok(goodsService.save(item));
    }

    @PutMapping
    @Log(describe = "修改商品")
    public R<?> edit(@RequestBody SysGoods item) {
        return R.ok(goodsService.updateById(item));
    }

    @PutMapping("/editStatus")
    @Log(describe = "商品商家/下架")
    public R<?> editStatus(@RequestBody SysGoods item) {
        LambdaUpdateWrapper<SysGoods> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysGoods::getGrounding, item.getGrounding());
        wrapper.eq(SysGoods::getId, item.getId());
        goodsService.update(wrapper);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除商品")
    public R<?> delete(@PathVariable Long id) {
        return R.ok(goodsService.removeById(id));
    }

    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取商品信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(goodsService.getById(id));
    }

    @GetMapping("/getAll")
    @Log(describe = "获取所有商品信息")
    public R<?> getAll(SysGoods item) {
        PageUtils.start();
        List<SysGoods> list = goodsService.lambdaQuery()
                .like(item.getName() != null, SysGoods::getName, item.getName())
                .like(item.getType() != null, SysGoods::getType, item.getType())
                .eq(SysGoods::getGrounding, 1)
                .list();
        return R.ok(list);
    }

    @GetMapping("/editAddrNum/{id}/{name}/{num}")
    @Log(describe = "修改商品数量")
    public R<?> editAddrNum(@PathVariable("id") String id, @PathVariable("name") String name,
                            @PathVariable("num") Integer num) {
        SysGoods goods = goodsService.getById(id);
        if (goods == null) {
            return R.fail("商品不存在");
        }
        String attrStr = goods.getAttr();
        List<AttrVo> attrVos = JSON.parseArray(attrStr, AttrVo.class);

        AttrVo result = attrVos.stream()
                .filter(attr -> name.equals(attr.getName()))
                .findFirst()
                .orElse(null);

        if (result == null) {
            return R.fail("商品属性不存在");
        }

        for (AttrVo attrVo : attrVos) {
            if (name.equals(attrVo.getName())) {
                attrVo.setNum(attrVo.getNum() - num);
            }
        }

        String jsonString = JSON.toJSONString(attrVos);
        goods.setAttr(jsonString);
        goodsService.updateById(goods);
        return R.ok();
    }

    @GetMapping("/getType")
    @Log(describe = "获取所有类型")
    public R<?> getType(SysGoods goods) {
        List<SysGoods> list = goodsService.lambdaQuery()
                .like(goods.getName() != null, SysGoods::getName, goods.getName())
                .eq(SysGoods::getGrounding, 1)
                .list();

        Iterator<SysGoods> iterator = list.iterator();
        while (iterator.hasNext()) {
            SysGoods sysGoods = iterator.next();
            String type1 = sysGoods.getType();
            if (type1 == null) {
                iterator.remove();
                continue;
            }
            SysType type = typeService.lambdaQuery().eq(SysType::getId, sysGoods.getType()).one();
            if (type != null) {
                sysGoods.setType(type.getName());
            }
        }

        Map<String, List<SysGoods>> groupedByType = list.stream()
                .collect(Collectors.groupingBy(SysGoods::getType));
        return R.ok(groupedByType);
    }
}
