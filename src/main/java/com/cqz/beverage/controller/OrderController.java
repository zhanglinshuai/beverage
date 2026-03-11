package com.cqz.beverage.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.Orders;
import com.cqz.beverage.model.dto.orders.OrderDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.order.KeyWordRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.OrdersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 订单控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController implements Serializable {

    @Resource
    private OrdersService  ordersService;

    @GetMapping("/pageList")
    public Result<PageResponseDTO<OrderDTO>> getOrdersPage(HttpServletRequest request, PageRequest  pageRequest){
        if(request==null || pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<OrderDTO> orderList = ordersService.getOrderList(request, pageRequest);
        PageResponseDTO<OrderDTO> ordersPageResponseDTO = new PageResponseDTO<>();
        ordersPageResponseDTO.setTotal(orderList.getTotal());
        ordersPageResponseDTO.setRecords(orderList.getRecords());
        ordersPageResponseDTO.setPageNum((int) orderList.getCurrent());
        ordersPageResponseDTO.setPageSize((int) orderList.getSize());
        return Result.success(ordersPageResponseDTO);
    }

    @GetMapping("/current")
    public Result<OrderDTO> getCurrentOrder(HttpServletRequest request,String orderNo){
        if(StrUtil.isBlank(orderNo)){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        OrderDTO currentOrder = ordersService.getCurrentOrder(request, orderNo);
        if(currentOrder==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"获取订单信息失败");
        }
        return Result.success(currentOrder);
    }

    @GetMapping("/keyword/list")
    public Result<PageResponseDTO<OrderDTO>> getPageListByKeyWord(HttpServletRequest request, KeyWordRequest keyWordRequest,PageRequest pageRequest){
        if(request==null || keyWordRequest==null ||  pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<OrderDTO> orderListByKeyWord = ordersService.getOrderListByKeyWord(request, pageRequest, keyWordRequest);
        PageResponseDTO<OrderDTO> ordersPageResponseDTO = new PageResponseDTO<>();
        ordersPageResponseDTO.setTotal(orderListByKeyWord.getTotal());
        ordersPageResponseDTO.setRecords(orderListByKeyWord.getRecords());
        ordersPageResponseDTO.setPageNum((int) orderListByKeyWord.getCurrent());
        ordersPageResponseDTO.setPageSize((int) orderListByKeyWord.getSize());
        return Result.success(ordersPageResponseDTO);
    }
}
