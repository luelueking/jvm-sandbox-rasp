package com.lue.rasp.hook.dubbo;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lue.rasp.config.HookConfig;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;


// TODO
//@MetaInfServices(Module.class)
//@Information(id = "rasp-dubbo-hook" , author = "1ue" , version = "0.0.1")
public class DubboRequestHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;


    public void hookDubboRequest() {
//        new EventWatchBuilder(moduleEventWatcher)
//                // alibaba.dubbo || apache.dubbo
//                .onClass("com.*.dubbo.rpc.filter.ContextFilter")
//                ;
    }

    @Override
    public void onLoad() throws Throwable {

    }

    @Override
    public void onUnload() throws Throwable {

    }

    @Override
    public void onActive() throws Throwable {

    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        if (HookConfig.isEnable("dubbo")) {
            hookDubboRequest();
        }
        System.out.println("rasp的RCE-HOOK加载完成！！！");
    }
}
