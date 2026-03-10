package com.cqz.beverage.model.vo.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加商品配置所用到的请求参数
 */
@Data
public class AddInventoryRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceCode;
    private String brand;
    private String productName;
    private String channelNo;
    private Integer stock;
    private Integer maxCapacity;
    private Integer warningStock;

}
