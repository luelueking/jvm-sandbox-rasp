# jvm-sandbox-rasp
一个基于jvm-sandbox高度定制化rasp
### Quick-start
- 将Rasp-All/target/Rasp-All-1.0-SNAPSHOT-jar-with-dependencies.jar放入${sanbox-home}/module目录下
```shell
cd ${sandbox-home}/bin // 进入sandbox
jps -l // 查看选择attach的java进程
./sandbox.h -p 85794 -R // 刷新所有module
./sandbox.h -p 85794 -l // 查看加载module列表
./sandbox.h -p 85794 -u 'module-name' // 卸载module
```
- 其他参数[参考](https://github.com/alibaba/jvm-sandbox/wiki/USER-INSTALL-and-CONFIG)
### 功能列表
- rce防御(hook native)
- sql注入防御
- xxe注入防御
- deserialize防御
- 线程注入防御
- JNI注入防御
- websocket埋点
- File类hook
- method埋点(hook reflect)
### 优点
- 与jvm-sandbox集成(支持module热插拔)
- 完全的类加载器隔离(不会出现openrasp类加载模式导致的类冲突问题)
### 原理说明
- [类隔离？如何hook native？上下文分析？](http://www.luelueking.com/archives/yi-jvm-sandboxwei-ji-chu-qian-tan-raspde-shi-xian-yao-dian-shang)
- [防线程注入？防JNI注入？ws协议hook？堆栈输出？](http://www.luelueking.com/archives/yi-jvm-sandboxwei-ji-chu-qian-tan-raspshi-xian-yao-dian-zhong)
- [反射hook？web控制台构想？日志管理？](http://www.luelueking.com/archives/1691401956791)
### 备注
- jvm-sandbox-rasp-web-backend为后端代码
- jvm-sandbox-rasp-web-agent为agent的代码，也是rasp的核心逻辑
