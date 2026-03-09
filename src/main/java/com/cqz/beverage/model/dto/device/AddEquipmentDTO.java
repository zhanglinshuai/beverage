package com.cqz.beverage.model.dto.device;

import com.cqz.beverage.model.Device;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 添加设备的响应参数
 */
@Data
public class AddEquipmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceCode;
    private String deviceName;
    private String deviceModel;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String status;
    private Integer channelCount;
    private Long operationId;
    private Date installTime;
    private Date createTime;
    private Date updateTime;


    public static AddEquipmentDTO fromEntity(Device device){
        AddEquipmentDTO dto = new AddEquipmentDTO();
        dto.setDeviceCode(device.getDeviceCode());
        dto.setDeviceName(device.getDeviceName());
        dto.setDeviceModel(device.getDeviceModel());
        dto.setLocation(device.getLocation());
        dto.setLatitude(device.getLatitude());
        dto.setLongitude(device.getLongitude());
        dto.setStatus(device.getStatus());
        dto.setInstallTime(device.getInstallTime());
        dto.setCreateTime(device.getCreateTime());
        dto.setUpdateTime(device.getUpdateTime());
        dto.setOperationId(device.getOperationId());
        dto.setChannelCount(device.getChannelCount());
        return dto;
    }
}
