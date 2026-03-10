package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.DeviceChannel;
import com.cqz.beverage.service.DeviceChannelService;
import com.cqz.beverage.mapper.DeviceChannelMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【device_channel(货道表-设备中包含的货道)】的数据库操作Service实现
* @createDate 2026-03-09 15:58:31
*/
@Service
public class DeviceChannelServiceImpl extends ServiceImpl<DeviceChannelMapper, DeviceChannel>
    implements DeviceChannelService{

}




