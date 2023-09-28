package com.lue.rasp.config;

public class LogConfig {

    public static String logPath = "/tmp/rasp/logs";

    public static String getLogPath() {
        return logPath;
    }

    public static void setLogPath(String logPath) {
        LogConfig.logPath = logPath;
    }
}
