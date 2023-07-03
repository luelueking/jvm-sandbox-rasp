package com.lue.rasp.hook.ws;


import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lue.rasp.config.HookConfig;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

@MetaInfServices(Module.class)
@Information(id = "rasp-ws-hook" , author = "1ue" , version = "0.0.3")
public class WebsocketHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void hookWebsocket() {
        new EventWatchBuilder(moduleEventWatcher)
                .onClass(javax.websocket.MessageHandler.class)
                .includeBootstrap()
                .includeSubClasses()
                .onBehavior("onMessage")
                .onWatch(new AdviceListener(){
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到MessageHandler的onMessage方法");
                        Object o = advice.getParameterArray()[0];
                        System.out.println(o);
                        System.out.println(o.getClass());
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
        if (HookConfig.isEnable("ws")) {
            hookWebsocket();
        }
        System.out.println("rasp的WebSocket-HOOK加载完成！！！");
    }
}
