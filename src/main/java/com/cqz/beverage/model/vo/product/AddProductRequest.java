package com.cqz.beverage.model.vo.product;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增商品请求参数
 */
@Data
public class AddProductRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private String brand;
    private BigDecimal price;
    private String image;
    private String description;
    private String productType;

}
