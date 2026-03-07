package com.cqz.beverage.model.vo.device;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取设备信息的请求参数
 */
@Data
public class GetEquipmentInfoRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceCode;
    private HttpServletRequest request;
}
