package com.shop.user.controller;

import com.shop.core.domain.R;
import com.shop.core.domain.entity.SysAddr;
import com.shop.core.utils.PageUtils;
import com.shop.security.annotation.Log;
import com.shop.security.utils.SecurityUtils;
import com.shop.user.service.SysAddrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addr")
public class AddrController {

    @Autowired
    private SysAddrService addrService;

    @GetMapping("/list")
    @Log(describe = "查询地址列表")
    public R<?> list(SysAddr item) {
        PageUtils.start();
        List<SysAddr> list = addrService.lambdaQuery()
                .eq(SysAddr::getCreateBy, SecurityUtils.getUserId())
                .eq(SysAddr::getStatus, "0")
                .list();
        return R.ok(list);
    }

    @GetMapping("/getAll")
    @Log(describe = "获取所有地址")
    public R<?> getAll() {
        List<SysAddr> list = addrService.lambdaQuery()
                .eq(SysAddr::getCreateBy, SecurityUtils.getUserId())
                .eq(SysAddr::getStatus, "0")
                .list();
        return R.ok(list);
    }

    @PostMapping
    @Log(describe = "新增地址")
    public R<?> add(@RequestBody SysAddr item) {
        item.setCreateBy(SecurityUtils.getUserId());
        item.setStatus("0");
        return R.ok(addrService.save(item));
    }

    @PutMapping
    @Log(describe = "修改地址")
    public R<?> edit(@RequestBody SysAddr item) {
        return R.ok(addrService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @Log(describe = "删除地址")
    public R<?> delete(@PathVariable Long id) {
        SysAddr addr = new SysAddr();
        addr.setId(id.intValue());
        addr.setStatus("1");
        return R.ok(addrService.updateById(addr));
    }

    @GetMapping("/getById/{id}")
    @Log(describe = "根据id获取地址信息")
    public R<?> getById(@PathVariable String id) {
        return R.ok(addrService.getById(id));
    }
}
