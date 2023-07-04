package com.lue.rasp.controller;

import com.lue.rasp.entity.AppInstance;
import com.lue.rasp.entity.SysCfg;
import com.lue.rasp.service.AppInstanceService;
import com.lue.rasp.service.SysCfgService;
import com.lue.rasp.utils.R;
import com.lue.rasp.utils.RaspAgentUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@Api(tags = "实例管理")
@RestController
@RequestMapping("/instance")
public class InstanceController {

    @Autowired
    private AppInstanceService appInstanceService;

    @Autowired
    private SysCfgService sysCfgService;


    @ApiOperation("根据id查询实例")
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        AppInstance appInstance = appInstanceService.getById(id);
        return R.ok().put("appInstance", appInstance);
    }

    @ApiOperation("新建实例")
    @PostMapping("/save")
    public R save(@RequestBody AppInstance appInstance) throws IOException, InterruptedException {
        appInstanceService.save(appInstance);
        hookAgent(appInstance);
        return R.ok();
    }

    @ApiOperation("更新实例")
    @PostMapping("/update")
    public R update(@RequestBody AppInstance appInstance) throws IOException, InterruptedException {
        AppInstance oldInstance = appInstanceService.getById(appInstance.getAppPid());
        if (oldInstance.getAgentPort() != appInstance.getAgentPort()) {
            return R.error(500,"agent的端口无法更新");
        }
        appInstanceService.updateById(appInstance);
        return R.ok();
    }

    @ApiOperation("删除实例")
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        appInstanceService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    public void hookAgent(AppInstance appInstance) {
        try {
            SysCfg sysCfg = sysCfgService.list().get(0);
            String appPid = String.valueOf(appInstance.getAppPid());
            System.out.println(appPid);
            String agentPort = appInstance.getAgentPort();
            System.out.println(agentPort);
            RaspAgentUtils.startAgentByPort(sysCfg.getRaspHome(),appPid,agentPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
