package com.lue.rasp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lue.rasp.entity.SysCfg;
import com.lue.rasp.mapper.SysCfgMapper;
import com.lue.rasp.service.SysCfgService;
import org.springframework.stereotype.Service;

@Service("sysCfgService")
public class SysCfgServiceImpl extends ServiceImpl<SysCfgMapper, SysCfg> implements SysCfgService {
}
