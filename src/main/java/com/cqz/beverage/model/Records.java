package com.cqz.beverage.model;

import lombok.Data;

import java.util.Date;

/**
 * 维修档案记录所需要的字段值
 */
@Data
public class Records {
    private String maintenanceType;
    private String maintenanceContent;
    private Date maintenanceTime;
    private String operator;
}
