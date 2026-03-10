package com.cqz.beverage.model.vo.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * 脱敏后的设备信息
 */
@Data
public class DesensitizeDeviceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String  deviceCode;
    private String deviceName;

}
