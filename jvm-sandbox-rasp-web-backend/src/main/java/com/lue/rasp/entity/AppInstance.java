package com.lue.rasp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("app_instance")
public class AppInstance {
    @TableId
    private Long id;
    private String appName;
    private Long appPid;
    private String agentIp;
    private String agentPort;
    private String status;
}
