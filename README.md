# jvm-sandbox-rasp
一个基于jvm-sandbox高度定制化rasp
### Quick-start
- 将Rasp-All-1.0-SNAPSHOT-jar-with-dependencies.jar放入${sanbox-home}/module目录下
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
- 线程注入防御
- JNI注入防御
### 优点
- 与jvm-sandbox集成(支持module热插拔)
- 完全的类加载器隔离(不会出现openrasp类加载模式导致的类冲突问题)
