package com.cqz.beverage.model.vo.maintenance;

import com.cqz.beverage.model.DeviceArchive;
import com.cqz.beverage.model.Records;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑设备维修记录请求参数
 */
@Data
public class MotifyMaintenanceRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    //只渲染一遍的参数
    private String deviceCode;
    private String manufacturer;
    private Date productionDate;
    private Date installDate;
    private Date lastMaintenanceTime;
    private String warrantyPeriod;
    private String remark;
    //渲染多遍的参数
    private List<Records> records;






}
