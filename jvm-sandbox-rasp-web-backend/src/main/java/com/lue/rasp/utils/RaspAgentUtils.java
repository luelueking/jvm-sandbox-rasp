package com.lue.rasp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RaspAgentUtils {

    // TODO 直接exec丑陋+不安全
    public static boolean startAgentByPort(String raspHome, String pid, String port) throws IOException, InterruptedException {
        String shPath = raspHome + "/bin/sandbox.sh";
        // 构建命令和参数
        String[] command = {"sh", shPath, "-p", pid, "-P", port};
        try {
            // 创建进程构建器
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            // 设置工作目录
            processBuilder.directory(new File(raspHome+"/bin/"));
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            // 获取脚本输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String serverPort = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("SERVER_PORT")) {
                    serverPort = line.split(":")[1].trim();
                    break;
                }
            }
            // 获取标准错误流
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 读取错误输出并打印
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 打印输出结果和退出码
            System.out.println("脚本输出中的SERVER_PORT：" + serverPort);
            System.out.println("脚本退出码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
