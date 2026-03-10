package com.cqz.beverage.model.vo.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * 关键词搜索的请求参数
 */
@Data
public class KeyWordSearchRequest implements Serializable {
    private static final Long serialVersionUID = 1L;

    private String productName;
    private String brand;
    private String deviceCode;
}
