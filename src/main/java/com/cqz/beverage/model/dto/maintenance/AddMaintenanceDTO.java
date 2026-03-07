package com.cqz.beverage.model.dto.maintenance;

import com.cqz.beverage.model.DeviceArchive;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备维修档案录入响应
 */
@Data
public class AddMaintenanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String deviceCode;
    private String maintenanceType;
    private String maintenanceContent;
    private Date maintenanceTime;
    private String operator;
    private String manufacturer;
    private Date productionDate;
    private Date installDate;
    private String warrantyPeriod;
    private Date lastMaintenanceTime;
    private String remark;


    public static AddMaintenanceDTO fromEntity(DeviceArchive deviceArchive) {
        AddMaintenanceDTO dto = new AddMaintenanceDTO();
        dto.setId(deviceArchive.getId());
        dto.setDeviceCode(deviceArchive.getDeviceCode());
        dto.setMaintenanceType(deviceArchive.getMaintenanceType());
        dto.setMaintenanceContent(deviceArchive.getMaintenanceContent());
        dto.setMaintenanceTime(deviceArchive.getMaintenanceTime());
        dto.setOperator(deviceArchive.getOperator());
        dto.setManufacturer(deviceArchive.getManufacturer());
        dto.setProductionDate(deviceArchive.getProductionDate());
        dto.setInstallDate(deviceArchive.getInstallDate());
        dto.setWarrantyPeriod(deviceArchive.getWarrantyPeriod());
        dto.setLastMaintenanceTime(deviceArchive.getLastMaintenanceTime());
        dto.setRemark(deviceArchive.getRemark());
        return dto;
    }
}
