package com.lue.rasp.hook.jni;

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
@Information(id = "rasp-jni-hook" , author = "1ue" , version = "0.0.2")
public class JniHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;


    public void checkJNI() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(System.class)
                .includeBootstrap()
                .onBehavior("load")
                .onBehavior("loadLibrary")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到system的load方法");
                        RequestContextHolder.Context context = RequestContextHolder.getContext();
                        if (context != null) {
                            System.out.println(context);
                            System.out.println(context.getRequest().getRequestURI());
                            // 直接拦截
                            ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!!"));
                        }
                        super.before(advice);
                    }
                });
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(ClassLoader.class)
                .includeBootstrap()
                .onBehavior("loadLibrary0")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到classLoader的loadLibrary0方法");
                        RequestContextHolder.Context context = RequestContextHolder.getContext();
                        if (context != null) {
                            StackTrace.logTraceWithContext(context);
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
        System.out.println("rasp的JNI-HOOK卸载！！！");
    }

    @Override
    public void onActive() throws Throwable {
        System.out.println("rasp的JNI-HOOK激活！！！");
    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        if (HookConfig.isEnable("jni")) {
            checkJNI();
        }
        System.out.println("安装rasp的RCE-NATIVE-HOOK完毕！！！");
    }

}
