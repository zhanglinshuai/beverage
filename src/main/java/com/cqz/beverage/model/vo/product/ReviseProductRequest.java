package com.cqz.beverage.model.vo.product;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 编辑商品的请求参数
 */
@Data
public class ReviseProductRequest implements Serializable {
    private Long id;
    private String productName;
    private String brand;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer productStatus;
    private String productType;

}
