package com.cqz.beverage.model.dto.device;

import com.cqz.beverage.model.Device;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备详情响应参数
 */
@Data
public class EquipmentDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Device device;
    private String operatorName;
    private Long operatorId;
}
