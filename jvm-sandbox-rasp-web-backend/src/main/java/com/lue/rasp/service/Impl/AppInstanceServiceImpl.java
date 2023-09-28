package com.lue.rasp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lue.rasp.entity.AppInstance;
import com.lue.rasp.mapper.AppInstanceMapper;
import com.lue.rasp.service.AppInstanceService;
import org.springframework.stereotype.Service;

@Service("appInstanceService")
public class AppInstanceServiceImpl extends ServiceImpl<AppInstanceMapper, AppInstance> implements AppInstanceService {
}
