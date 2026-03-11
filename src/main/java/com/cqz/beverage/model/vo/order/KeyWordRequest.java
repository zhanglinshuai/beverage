package com.cqz.beverage.model.vo.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 通过关键词搜索的请求参数
 */
@Data
public class KeyWordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceCode;
    private String productName;
    private String brand;
    private String payType;
    private String orderStatus;
}
