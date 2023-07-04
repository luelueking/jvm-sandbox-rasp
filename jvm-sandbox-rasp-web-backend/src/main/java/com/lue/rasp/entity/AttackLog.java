package com.lue.rasp.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AttackLog {
    private String id;
    private String date; // 年月日字段
    private String time; // 时分秒字段
    private String uri;
    private String type;
    private String risk;
    private String status;
    private String logFileName;
}
