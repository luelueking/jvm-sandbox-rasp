package com.lue.rasp.hook.file;

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
import java.io.File;
import java.nio.file.Files;

@MetaInfServices(Module.class)
@Information(id = "rasp-file-hook", author = "1ue", version = "0.1.3")
public class FileHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /* 这里没有采取hook File的构造方法的原因是，hook后无法进行热插拔，破坏了agent的实现
        com.sun.tools.attach.AgentLoadException: Unable to add JAR file to system class path
	    at sun.tools.attach.HotSpotVirtualMachine.loadAgent(HotSpotVirtualMachine.java:119)*/
    private void checkFile() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(Files.class)
                .includeBootstrap()
                .onBehavior("readAllBytes")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        RequestContextHolder.Context context = RequestContextHolder.getContext();
                        if (null != context) {
                            java.nio.file.Path filePath = (java.nio.file.Path) advice.getParameterArray()[0];
                            if (filePath.toFile().getPath().contains("..")) {
                                System.out.println("evil file path: " + filePath);
                                System.out.println(filePath);
//                                StackTrace.logTraceWithContext(context);
                                StackTrace.logAttack(context,"pathTra","middle");
                                ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!! evil file path"));
                            }

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
        if (HookConfig.isEnable("file")) {
            checkFile();
        }
        System.out.println("RASP的File-HOOK加载完毕！！！");
    }


}
