package com.cqz.beverage.model.vo.product;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据关键词搜索商品
 */
@Data
public class KeyWordProductRequest implements Serializable {
    private String productName;
    private String brand;
    private String productType;
    private Integer productStatus;
}
