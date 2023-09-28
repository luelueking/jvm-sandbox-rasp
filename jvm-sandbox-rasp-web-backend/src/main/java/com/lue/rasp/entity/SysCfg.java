package com.lue.rasp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_config")
public class SysCfg {

    @TableId
    private Long id;
    private String raspHome;
}
