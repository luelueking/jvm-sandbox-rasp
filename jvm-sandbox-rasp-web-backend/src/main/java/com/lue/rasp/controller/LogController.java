package com.lue.rasp.controller;


import com.lue.rasp.entity.AttackLog;
import com.lue.rasp.service.LogService;
import com.lue.rasp.utils.FileUtils;
import com.lue.rasp.utils.PageUtils;
import com.lue.rasp.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Api(tags = "日志管理")
@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation("获取攻击日志列表")
    @GetMapping("/list")
    public Object list(){
        List list = logService.getList();
        return R.ok(list);
    }


    @ApiOperation("获取攻击日志堆栈信息")
    @GetMapping("/details/{id}")
    public Object getAttackStack(@PathVariable("id") String targetId) {
        List<AttackLog> list = logService.getList();
        AttackLog attackLog = list.stream().filter(log -> Objects.equals(log.getId(), targetId)).findFirst().get();
        String logFileName = attackLog.getLogFileName();
        return FileUtils.readLinesAfterSixth(logFileName);
    }
}
