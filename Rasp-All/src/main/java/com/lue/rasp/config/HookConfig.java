package com.lue.rasp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class HookConfig {
    public static HashMap<String,Boolean> hookConfig = new HashMap<String, Boolean>() {
        {
            put("http",true);
            put("jni",true);
            put("xxe",true);
            put("rce",true);
            put("sqli",true);
            put("deserialize",true);
        }
    };
    public static Boolean isEnable(String hook) {
        return hookConfig.get(hook);
    }
}
