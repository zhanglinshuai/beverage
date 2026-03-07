package com.cqz.beverage.model.vo.maintenance;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备档案录入请求
 */
@Data
public class AddMaintenanceRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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

    private HttpServletRequest request;
}
