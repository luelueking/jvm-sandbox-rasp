package com.lue.rasp.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static File getDateDir() {
        // 获取当前系统时间
        Date currentDate = new Date();
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 格式化日期为文件夹名称
        String folderName = dateFormat.format(currentDate);
        return new File(folderName);
    }

    public static String getDateDirName() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentDate);
    }

    public static String getTimeFileName() {
        // 生成文件名
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        String fileName = dateFormat.format(new Date());
        return fileName;
    }
}
