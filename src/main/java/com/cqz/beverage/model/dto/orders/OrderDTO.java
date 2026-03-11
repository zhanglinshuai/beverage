package com.cqz.beverage.model.dto.orders;

import cn.hutool.db.sql.Order;
import com.cqz.beverage.model.Orders;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单响应参数
 */
@Data
public class OrderDTO implements Serializable {
    private Long id;
    private String orderNo;
    private String deviceCode;
    private String productName;
    private String brand;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String payType;
    private String orderStatus;
    private Date createTime;

    public static OrderDTO fromEntity(Orders  entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setOrderNo(entity.getOrderNo());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setPayType(entity.getPayType());
        dto.setOrderStatus(entity.getOrderStatus());
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }
}
