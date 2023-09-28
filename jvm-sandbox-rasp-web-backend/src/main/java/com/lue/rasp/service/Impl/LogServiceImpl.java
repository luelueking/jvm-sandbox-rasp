package com.lue.rasp.service.Impl;

import com.lue.rasp.entity.AttackLog;
import com.lue.rasp.service.LogService;
import com.lue.rasp.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("logService")
public class LogServiceImpl implements LogService {


    @Override
    public ArrayList<AttackLog> getList() {
        ArrayList<AttackLog> attackLogs = new ArrayList<>();
        List<String> logDirs= FileUtils.listMatchingDirectories("/tmp","yyyy-MM-dd");
        logDirs.forEach(System.out::println);
        for (String logDir : logDirs) {
            List<String> logFiles= FileUtils.listFiles("/tmp/"+logDir);
            for (String logFile : logFiles) {
                System.out.println(logFile);
                String[] parts = logFile.split("\\.");
                String[] split = parts[0].split("/");
                AttackLog attackLog = new AttackLog();
                attackLog.setLogFileName(logFile);
                attackLog.setId(split[3]);
                attackLog.setDate(split[2]);
                attackLog.setTime(FileUtils.readLineFromFile(logFile,1));
                attackLog.setType(FileUtils.readLineFromFile(logFile,2));
                attackLog.setRisk(FileUtils.readLineFromFile(logFile,3));
                attackLog.setUri(FileUtils.readLineFromFile(logFile, 4));
                attackLog.setStatus(FileUtils.readLineFromFile(logFile, 5));
                System.out.println(attackLog);
                attackLogs.add(attackLog);
            }
        }
        return attackLogs;
    }
}
