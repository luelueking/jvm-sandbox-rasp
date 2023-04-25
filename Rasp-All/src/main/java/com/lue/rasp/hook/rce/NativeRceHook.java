package com.lue.rasp.hook.rce;

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


@MetaInfServices(Module.class)
@Information(id = "rasp-rce-native-hook" , author = "1ue" , version = "0.0.5")
public class NativeRceHook implements Module, ModuleLifecycle {


    @Resource
    private ModuleEventWatcher moduleEventWatcher;


    public void checkRceCommand() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("java.lang.UNIXProcess")
                .includeBootstrap()
                .onBehavior("forkAndExec")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到native的forkAndExec方法");
                        // TODO 对上下文进行分析判断
                        RequestContextHolder.Context context = RequestContextHolder.getContext();
                        if (context != null) {
                            System.out.println(context);
                            System.out.println(context.getRequest().getRequestURI());
                            System.out.println("开始打印StackTrace");
                            for (String s : StackTrace.getStackTraceString()) {
                                System.out.println(s);
                            }
                            // 直接拦截
                            ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!!"));
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
        System.out.println("rasp的RCE-NATIVE-HOOK卸载！！！");
    }

    @Override
    public void onActive() throws Throwable {
        System.out.println("rasp的RCE-NATIVE-HOOK激活！！！");
    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        if (HookConfig.isEnable("rce")) {
            checkRceCommand();
        }
        System.out.println("安装rasp的RCE-NATIVE-HOOK完毕！！！");
    }
}
