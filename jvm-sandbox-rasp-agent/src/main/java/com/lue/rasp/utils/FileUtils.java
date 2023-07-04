package com.lue.rasp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    public static void writeFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("File write operation completed.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing the file: " + e.getMessage());
        }
    }

    public static void appendToFile(String filePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            System.out.println("File append operation completed.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file: " + e.getMessage());
        }
    }

    public static void createDir(String directoryPath) {
        File directory = new File(directoryPath);

        // 检查文件夹是否已存在
        if (!directory.exists()) {
            // 使用mkdir()方法创建文件夹
            boolean created = directory.mkdir();

            if (created) {
                System.out.println("文件夹创建成功！");
            } else {
                System.out.println("文件夹创建失败！");
            }
        } else {
            System.out.println("文件夹已存在！");
        }
    }
}
