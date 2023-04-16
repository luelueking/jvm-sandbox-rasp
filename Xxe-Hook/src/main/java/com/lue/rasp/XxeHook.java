package com.lue.rasp;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.dom4j.io.SAXReader;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;


@MetaInfServices(Module.class)
@Information(id = "rasp-xxe-hook" , author = "1ue" , version = "0.0.3")
public class XxeHook implements Module, ModuleLifecycle {

    private static final String FEATURE_DEFAULTS_1 = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String FEATURE_DEFAULTS_2 = "http://xml.org/sax/features/external-general-entities";
    private static final String FEATURE_DEFAULTS_3 = "http://xml.org/sax/features/external-parameter-entities";
    private static final String FEATURE_DEFAULTS_4 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    public void xxeDefense() {
        // org.dom4j.io.SAXReader
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("org.dom4j.io.SAXReader")
                .onBehavior("read")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        org.dom4j.io.SAXReader saxReader = (SAXReader) advice.getTarget();
                        saxReader.setFeature(FEATURE_DEFAULTS_1, true);
                        super.before(advice);
                    }
                });
        // org.jdom.input.SAXBuilder
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("org.jdom.input.SAXBuilder")
                .onBehavior("build")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        org.jdom.input.SAXBuilder saxBuilder = (org.jdom.input.SAXBuilder) advice.getTarget();
                        saxBuilder.setFeature(FEATURE_DEFAULTS_1, true);
                        super.before(advice);
                    }
                });
        // org.jdom2.input.SAXBuilder
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("org.jdom2.input.SAXBuilder")
                .onBehavior("build")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        org.jdom2.input.SAXBuilder saxBuilder = (org.jdom2.input.SAXBuilder) advice.getTarget();
                        saxBuilder.setFeature(FEATURE_DEFAULTS_1, true);
                        super.before(advice);
                    }
                });
        // javax.xml.parsers.DocumentBuilderFactory
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("javax.xml.parsers.DocumentBuilderFactory")
                .includeBootstrap()
                .onBehavior("newInstance")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        System.out.println("hook到DocumentBuilderFactory#newInstance方法");
                        super.before(advice);
                    }

                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        javax.xml.parsers.DocumentBuilderFactory instance = (javax.xml.parsers.DocumentBuilderFactory) advice.getReturnObj();
                        // 这个是基本的防御方式。 如果DTDs被禁用, 能够防止绝大部分的XXE;
                        // 如果这里设置为true会影响mybatis-xml的加载
                        instance.setFeature(FEATURE_DEFAULTS_1, true);
                        // 如果不能完全禁用DTDs，至少下面的几个需要禁用
                        instance.setFeature(FEATURE_DEFAULTS_2, false);
                        instance.setFeature(FEATURE_DEFAULTS_3, false);
                        instance.setFeature(FEATURE_DEFAULTS_4, false);
                        instance.setXIncludeAware(false);
                        instance.setExpandEntityReferences(false);
                    }
                });
        // javax.xml.stream.XMLInputFactory
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("javax.xml.stream.XMLInputFactory")
                .includeBootstrap()
                .onBehavior("newInstance")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        javax.xml.stream.XMLInputFactory xmlInputFactory = (javax.xml.stream.XMLInputFactory) advice.getReturnObj();
                        // This disables DTDs entirely for that factory
                        // 下面是open-rasp 的防护策略
                        xmlInputFactory.setProperty(javax.xml.stream.XMLInputFactory.SUPPORT_DTD, false);
                        // 下面的2个是owasp建议，启用之后，调用会报错
                        // This causes XMLStreamException to be thrown if external DTDs are accessed.
                        // java.lang.IllegalArgumentException: Unrecognized property 'http://javax.xml.XMLConstants/property/accessExternalDTD'
                        // xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                        // disable external entities
                        // xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
                    }
                });
        // org.xml.sax.helpers.XMLReaderFactory
        new EventWatchBuilder(moduleEventWatcher)
                .onClass("org.xml.sax.helpers.XMLReaderFactory")
                .onBehavior("createXMLReader")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        org.xml.sax.XMLReader xmlReader = (org.xml.sax.XMLReader) advice.getReturnObj();
                        // 这个是基本的防御方式。 如果DTDs被禁用, 能够防止绝大部分的XXE;
                        xmlReader.setFeature(FEATURE_DEFAULTS_1, true); // todo
                        // 如果不能完全禁用DTDs，至少下面的几个需要禁用:(推荐)
                        xmlReader.setFeature(FEATURE_DEFAULTS_2, false);
                        xmlReader.setFeature(FEATURE_DEFAULTS_3, false);
                        xmlReader.setFeature(FEATURE_DEFAULTS_4, false);
                    }
                });
    }

    @Override
    public void onLoad() throws Throwable {

    }

    @Override
    public void onUnload() throws Throwable {
        System.out.println("rasp的Xxe-HOOK卸载！！！");
    }

    @Override
    public void onActive() throws Throwable {
        System.out.println("rasp的Xxe-HOOK激活！！！");
    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        xxeDefense();
        System.out.println("rasp的Xxe-HOOK加载完毕");
    }
}
