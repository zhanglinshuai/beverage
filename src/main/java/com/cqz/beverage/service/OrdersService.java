package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.orders.OrderDTO;
import com.cqz.beverage.model.vo.order.KeyWordRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
* @author zhanglinshuai
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2026-03-06 14:45:53
*/
public interface OrdersService extends IService<Orders> {
    /**
     * 分页获取订单列表
     * @param req
     * @param request
     * @return
     */
    IPage<OrderDTO> getOrderList(HttpServletRequest req, PageRequest request);

    /**
     * 获取当前订单信息
     * @param req
     * @param orderNo
     * @return
     */
    OrderDTO getCurrentOrder(HttpServletRequest req, String orderNo);

    /**
     * 查询订单
     * @param req
     * @return
     */
    IPage<OrderDTO> getOrderListByKeyWord(HttpServletRequest req, PageRequest request, KeyWordRequest keyWordRequest);
}
