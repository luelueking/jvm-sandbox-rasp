package com.lue.rasp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtils {

    public static List<String> listMatchingDirectories(String directoryPath, String dateFormatPattern) {
        List<String> matchingDirectories = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String directoryName = file.getName();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                    dateFormat.setLenient(false);  // 确保严格匹配日期格式

                    try {
                        Date date = dateFormat.parse(directoryName);
                        matchingDirectories.add(directoryName);
                    } catch (java.text.ParseException e) {
                        // 当前文件夹名不符合日期格式，忽略
                    }
                }
            }
        }

        return matchingDirectories;
    }

    public static List<String> listFiles(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        List<String> subFiles = listFiles(file.getAbsolutePath());
                        fileList.addAll(subFiles);
                    }
                }
            }
        }

        return fileList;
    }


    public static String readLineFromFile(String filePath, int targetLine) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 1;

            while ((line = reader.readLine()) != null) {
                if (currentLine == targetLine) {
                    return line;
                }

                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readLinesAfterSixth(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            // 跳过前五行
            while (lineCount < 5 && (line = reader.readLine()) != null) {
                lineCount++;
            }

            // 读取第六行以后的内容
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
