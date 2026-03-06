package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.Inventory;
import com.cqz.beverage.service.InventoryService;
import com.cqz.beverage.mapper.InventoryMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【inventory(库存表-存储各个设备的各个商品还有多少库存)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
    implements InventoryService{

}




