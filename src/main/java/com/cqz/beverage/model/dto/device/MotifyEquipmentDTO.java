package com.cqz.beverage.model.dto.device;

import com.cqz.beverage.model.Device;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 修改设备的响应参数
 */
@Data
public class MotifyEquipmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceName;
    private String deviceCode;
    private String deviceModel;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private Long operationId;
    private Date installTime;
    private Integer channelCount;


    public static MotifyEquipmentDTO fromEntity(Device device) {
        MotifyEquipmentDTO dto = new MotifyEquipmentDTO();
        dto.setDeviceName(device.getDeviceName());
        dto.setDeviceCode(device.getDeviceCode());
        dto.setDeviceModel(device.getDeviceModel());
        dto.setLocation(device.getLocation());
        dto.setLatitude(device.getLatitude());
        dto.setLongitude(device.getLongitude());
        dto.setStatus(device.getStatus());
        dto.setInstallTime(device.getInstallTime());
        dto.setOperationId(device.getOperationId());
        dto.setChannelCount(device.getChannelCount());
        return dto;
    }
}
