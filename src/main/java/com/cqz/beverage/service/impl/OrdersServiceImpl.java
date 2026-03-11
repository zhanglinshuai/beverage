package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.ProductMapper;
import com.cqz.beverage.mapper.UserMapper;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.Orders;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.orders.OrderDTO;
import com.cqz.beverage.model.vo.order.KeyWordRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceService;
import com.cqz.beverage.service.OrdersService;
import com.cqz.beverage.mapper.OrdersMapper;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Resource
    private DeviceService  deviceService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserService userService;
    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private ProductMapper productMapper;
    @Override
    public IPage<OrderDTO> getOrderList(HttpServletRequest req, PageRequest request) {
        if (req==null || request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        boolean adminAndOperator = isAdminAndOperator(req);
        if(!adminAndOperator){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非管理员或运营商无权限");
        }
        Page<Orders> ordersPage = new Page<>(request.getPageNum(), request.getPageSize());
        ordersMapper.selectPage(ordersPage,null);
        Page<OrderDTO> orderDTOPage = new Page<>();
        List<OrderDTO> records = new ArrayList<>();
        for(Orders orders: ordersPage.getRecords()){
            OrderDTO orderDTO = OrderDTO.fromEntity(orders);
            //获取设备编号
            Long deviceId = orders.getDeviceId();
            Device device = deviceService.getById(deviceId);
            orderDTO.setDeviceCode(device.getDeviceCode());
            //获取商品名称和品牌
            Long productId = orders.getProductId();
            Product product = productMapper.selectById(productId);
            orderDTO.setBrand(product.getBrand());
            orderDTO.setProductName(product.getProductName());
            records.add(orderDTO);
        }
        orderDTOPage.setRecords(records);
        orderDTOPage.setTotal(ordersPage.getTotal());
        orderDTOPage.setCurrent(ordersPage.getCurrent());
        orderDTOPage.setSize(ordersPage.getSize());
        return orderDTOPage;
    }

    @Override
    public OrderDTO getCurrentOrder(HttpServletRequest req, String orderNo) {
        if(orderNo==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        boolean adminAndOperator = isAdminAndOperator(req);
        if(!adminAndOperator){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        QueryWrapper<Orders> wrapper = new QueryWrapper<Orders>().eq("order_no", orderNo);
        Orders orders = ordersMapper.selectOne(wrapper);
        OrderDTO orderDTO = OrderDTO.fromEntity(orders);
        //获取设备编号
        Long deviceId = orders.getDeviceId();
        Device device = deviceService.getById(deviceId);
        orderDTO.setDeviceCode(device.getDeviceCode());
        //获取商品名称、商品品牌
        Long productId = orders.getProductId();
        Product product = productMapper.selectById(productId);
        orderDTO.setBrand(product.getBrand());
        orderDTO.setProductName(product.getProductName());
        return orderDTO;
    }

    @Override
    public IPage<OrderDTO> getOrderListByKeyWord(HttpServletRequest req, PageRequest request, KeyWordRequest keyWordRequest) {
        if (req==null || request==null ||  keyWordRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        boolean adminAndOperator = isAdminAndOperator(req);
        if(!adminAndOperator){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        Page<Orders> ordersPage = new Page<>(request.getPageNum(), request.getPageSize());
        String orderStatus = keyWordRequest.getOrderStatus();
        String brand = keyWordRequest.getBrand();
        String payType = keyWordRequest.getPayType();
        String deviceCode = keyWordRequest.getDeviceCode();
        String productName = keyWordRequest.getProductName();
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
        Device device = deviceService.getOne(deviceQueryWrapper);
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        //拼接查询条件
        if(orderStatus!=null){
            ordersQueryWrapper.eq("order_status", orderStatus);
        }
        if(payType!=null){
            ordersQueryWrapper.eq("pay_type", payType);
        }
        if(deviceCode!=null){

            ordersQueryWrapper.eq("device_id", device.getId());
        }
        if(productName!=null && brand!=null){
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>().eq("product_name", productName).eq("brand", brand);
            Product product = productMapper.selectOne(productQueryWrapper);
            ordersQueryWrapper.eq("product_id", product.getId());
        }
        ordersMapper.selectPage(ordersPage,ordersQueryWrapper);
        Page<OrderDTO> orderDTOPage = new Page<>();
        List<OrderDTO> records = new ArrayList<>();
        for(Orders orders: ordersPage.getRecords()){
            OrderDTO orderDTO = OrderDTO.fromEntity(orders);
            orderDTO.setDeviceCode(device.getDeviceCode());
            Long productId = orders.getProductId();
            Product product = productMapper.selectById(productId);
            orderDTO.setBrand(product.getBrand());
            orderDTO.setProductName(product.getProductName());
            records.add(orderDTO);
        }
        orderDTOPage.setRecords(records);
        orderDTOPage.setTotal(ordersPage.getTotal());
        orderDTOPage.setCurrent(ordersPage.getCurrent());
        orderDTOPage.setSize(ordersPage.getSize());
        return orderDTOPage;

    }

    private boolean isAdminAndOperator(HttpServletRequest req) {
        String header = req.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        return deviceService.isAdminAndOperator(currentUser);
    }
}




