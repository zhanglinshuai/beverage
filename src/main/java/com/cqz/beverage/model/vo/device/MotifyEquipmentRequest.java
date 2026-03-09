package com.cqz.beverage.model.vo.device;

import com.cqz.beverage.model.Device;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改设备的请求参数
 */
@Data
public class MotifyEquipmentRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Device device;

    private String deviceName;
    private String deviceCode;
    private String deviceModel;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private Long operationId;
    private Integer channelCount;
    private Date installTime;
}
