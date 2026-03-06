package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.WorkOrder;
import com.cqz.beverage.service.WorkOrderService;
import com.cqz.beverage.mapper.WorkOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【work_order(工单表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:41
*/
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder>
    implements WorkOrderService{

}




