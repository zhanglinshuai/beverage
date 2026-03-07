package com.cqz.beverage.model.vo.maintenance;

import com.cqz.beverage.model.DeviceArchive;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 编辑设备维修记录请求参数
 */
@Data
public class MotifyMaintenanceRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private HttpServletRequest request;
    private DeviceArchive deviceArchive;

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
}
