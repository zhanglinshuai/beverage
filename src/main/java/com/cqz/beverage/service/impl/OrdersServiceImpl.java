package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.Orders;
import com.cqz.beverage.service.OrdersService;
import com.cqz.beverage.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




