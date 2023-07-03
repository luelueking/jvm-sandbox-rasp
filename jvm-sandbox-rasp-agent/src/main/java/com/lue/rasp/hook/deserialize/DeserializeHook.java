package com.lue.rasp.hook.deserialize;

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
import java.io.ObjectInputStream;


@MetaInfServices(Module.class)
@Information(id = "rasp-deserialize-hook" , author = "1ue" , version = "0.0.1")
public class DeserializeHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void checkDeserialize() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(ObjectInputStream.class)
                .includeBootstrap()
                .onBehavior("readObject")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到readObject方法");
                        // TODO 上下文分析
                        RequestContextHolder.Context context = RequestContextHolder.getContext();
                        if (null != context) {
                            StackTrace.logTraceWithContext(context);
                        }
                        ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!!"));
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
        if (HookConfig.isEnable("deserialize")) {
            checkDeserialize();
        }
        System.out.println("RASP的Deserialize-HOOK加载完毕！！！");
    }
}
