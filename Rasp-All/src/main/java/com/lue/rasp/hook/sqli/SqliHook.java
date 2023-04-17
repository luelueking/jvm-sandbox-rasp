package com.lue.rasp.hook.sqli;


import com.alibaba.druid.wall.WallUtils;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

// 目前只支持Mysql,因为是使用的druid的WallUtils来检测，所以在module中要选择xxx-with-dependencies.jar
@MetaInfServices(Module.class)
@Information(id = "rasp-sqli-hook" , author = "1ue" , version = "0.0.3")
public class SqliHook implements Module, ModuleLifecycle {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;


    public void checkSql() {
        // mysql8.x
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.mysql.cj.jdbc.StatementImpl")
                .onBehavior("executeQuery")
                .onBehavior("execute")
                .onBehavior("executeUpdate")
                .onBehavior("addBatch")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        String sql = (String) advice.getParameterArray()[0];
                        System.out.println("hook到sql语句：" + sql);
                        // TODO 添加上下文
                        boolean validateMySql = WallUtils.isValidateMySql(sql);
                        System.out.println(validateMySql);
                        if (!validateMySql) {
                            ProcessController.throwsImmediately(new RuntimeException("Block By RASP!!!"));
                        }
                        super.before(advice);
                    }
                });

        // mysql5.x
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.mysql.jdbc.StatementImpl")
                .onBehavior("executeInternal")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdateInternal")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        String sql = (String) advice.getParameterArray()[0];
                        System.out.println("hook到sql语句：" + sql);
                        // TODO 添加上下文
                        boolean validateMySql = WallUtils.isValidateMySql(sql);
                        System.out.println(validateMySql);
                        if (!validateMySql) {
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
        System.out.println("rasp的Sqli-HOOK卸载！！！");
    }

    @Override
    public void onActive() throws Throwable {
        System.out.println("rasp的Sqli-HOOK激活！！！");
    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        checkSql();
        System.out.println("rasp的Sqli-Hook加载完毕");
    }
}
