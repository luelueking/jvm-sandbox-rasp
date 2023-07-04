package com.lue.rasp.controller;

import com.dtflys.forest.Forest;
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
import java.util.Map;

@Api(tags = "实例管理")
@RestController
@RequestMapping("/instance")
public class InstanceController {

    @Autowired
    private AppInstanceService appInstanceService;

    @Autowired
    private SysCfgService sysCfgService;

    @ApiOperation("根据id查询实例agent加载模块列表")
    @GetMapping("/agent/modules/list/{id}")
    public Object agentModules(@PathVariable("id") Long id) {
        AppInstance instance = appInstanceService.getById(id);
        Object result = getAgentInfo(instance, "/sandbox/default/module/http/sandbox-module-mgr/list?1=1");
        return R.ok(result);
    }
    @ApiOperation("根据id查询实例agent信息Info")
    @GetMapping("/agent/info/{id}")
    public Object agentInfo(@PathVariable("id") Long id) {
        AppInstance instance = appInstanceService.getById(id);
        Object result = getAgentInfo(instance,"/sandbox/default/module/http/sandbox-info/version?1=1");
        return R.ok(result);
    }

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

    public Object getAgentInfo(AppInstance instance, String url) {
        return Forest.get(url)
                .backend("okhttp3")        // 设置后端为 okhttp3
                .host(instance.getAgentIp())
                .port(Integer.parseInt(instance.getAgentPort()))
                .maxRetryCount(3)          // 设置请求最大重试次数为 3
                // 设置请求成功判断条件回调函数
                .successWhen((req, res) -> res.noException() && res.statusOk())
                .execute();
    }
}
