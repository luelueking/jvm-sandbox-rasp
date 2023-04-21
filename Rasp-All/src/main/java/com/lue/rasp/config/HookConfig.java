package com.lue.rasp.config;

import java.util.HashMap;

public class HookConfig {
    public static HashMap<String,Boolean> hookConfig = new HashMap<String, Boolean>() {
        {
            put("http",true);
            put("jni",true);
            put("xxe",true);
            put("rce",true);
            put("sqli",true);
            put("deserialize",true);
            put("ws",true);
            put("dubbo",true);
        }
    };
    public static Boolean isEnable(String hook) {
        return hookConfig.get(hook);
    }
}
