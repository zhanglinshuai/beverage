package com.cqz.beverage.model.vo.device;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加设备的请求参数
 */
@Data
public class AddEquipmentRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceCode;
    private String deviceName;
    private String deviceModel;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String status;
    private Long operationId;
}
