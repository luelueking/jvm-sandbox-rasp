package com.lue.rasp.controller;

import com.lue.rasp.entity.SysCfg;
import com.lue.rasp.service.SysCfgService;
import com.lue.rasp.utils.PageUtils;
import com.lue.rasp.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Api(tags = "系统设置")
@RestController
@RequestMapping("/sysCfg")
public class SysController {

    @Autowired
    private SysCfgService sysCfgService;

    @ApiOperation("查询SysCfg")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SysCfg departmentInfo = sysCfgService.getById(id);

        return R.ok().put("sysCfg", departmentInfo);
    }

    @ApiOperation("新建SysCfg")
    @PostMapping("/save")
    public R save(@RequestBody SysCfg sysCfg){
        sysCfgService.save(sysCfg);
        return R.ok();
    }

    @ApiOperation("更新SysCfg")
    @PostMapping("/update")
    public R update(@RequestBody SysCfg sysCfg){
        sysCfgService.updateById(sysCfg);
        return R.ok();
    }

    @ApiOperation("删除SysCfg")
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        sysCfgService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
