package com.lue.rasp.hook.reflect;


import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lue.rasp.config.HookConfig;
import com.lue.rasp.context.RequestContextHolder;
import com.lue.rasp.utils.StackTrace;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.file.Files;

@MetaInfServices(Module.class)
@Information(id = "rasp-reflect-hook", author = "1ue", version = "0.0.1")
public class ReflectHook implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    private void checkReflect() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(Method.class)
                .includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        Method target = (Method) advice.getTarget();
                        if (target.getName().endsWith("forkAndExec")) {
                            RequestContextHolder.Context context = RequestContextHolder.getContext();
                            StackTrace.logAttack(context, "reflect", "high");
                            ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!! Bad Boy"));
                        }
                        super.before(advice);
                    }
                });
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
        if (HookConfig.isEnable("reflect")) {
            checkReflect();
        }
        System.out.println("RASP的Reflect-HOOK加载完毕！！！");
    }


}
