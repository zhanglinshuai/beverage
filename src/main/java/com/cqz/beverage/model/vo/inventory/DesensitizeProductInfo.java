package com.cqz.beverage.model.vo.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * 脱敏后的商品信息
 */
@Data
public class DesensitizeProductInfo implements Serializable {
    private static final Long  serialVersionUID = 1L;

    private String brand;
    private String productName;

}
