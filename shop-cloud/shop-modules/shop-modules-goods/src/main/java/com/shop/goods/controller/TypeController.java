package com.shop.goods.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysType;
import com.shop.core.utils.PageUtils;
import com.shop.goods.service.SysTypeService;
import com.shop.security.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private SysTypeService typeService;

    @GetMapping("/list")
    @Log(describe = "查询环保类型列表")
    public R<?> list(SysType item) {
        PageUtils.start();
        List<SysType> list = typeService.lambdaQuery()
                .like(item.getName() != null, SysType::getName, item.getName())
                .list();
        return R.ok(list);
    }

    @GetMapping("/getAll")
    @Log(describe = "获取所有环保类型")
    public R<?> getAll() {
        return R.ok(typeService.list());
    }

    @PostMapping
    @Log(describe = "新增环保类型")
    public R<?> add(@RequestBody SysType item) {
        return R.ok(typeService.save(item));
    }

    @PutMapping
    @Log(describe = "修改环保类型")
    public R<?> edit(@RequestBody SysType item) {
        return R.ok(typeService.updateById(item));
    }

    @PutMapping("/editStatus")
    @Log(describe = "修改环保类型状态")
    public R<?> editStatus(@RequestBody SysType item) {
        LambdaUpdateWrapper<SysType> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysType::getStatus, item.getStatus());
        wrapper.eq(SysType::getId, item.getId());
        return R.ok(typeService.update(wrapper));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除环保类型")
    public R<?> delete(@PathVariable Long id) {
        return R.ok(typeService.removeById(id));
    }

    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取环保类型")
    public R<?> getById(@PathVariable Long id) {
        return R.ok(typeService.getById(id));
    }
}
