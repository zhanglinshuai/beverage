package com.cqz.beverage.model.dto.device;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除设备的响应参数
 */
@Data
public class DeleteEquipmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String equipmentCode;
    private  boolean isDelete;
}
