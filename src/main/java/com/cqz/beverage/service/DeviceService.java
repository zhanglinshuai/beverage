package com.cqz.beverage.service;

import com.cqz.beverage.model.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.vo.device.AddEquipmentRequest;
import com.cqz.beverage.model.vo.device.DeleteEquipmentRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zhanglinshuai
* @description 针对表【device(设备表)】的数据库操作Service，用户需要是管理员或者是运营商才能够对设备继续管理
* @createDate 2026-03-06 14:45:53
*/
public interface DeviceService extends IService<Device> {

    /**
     * 添加设备
     * @param request
     * @param addEquipmentRequest
     * @return
     */
    AddEquipmentDTO addEquipment(HttpServletRequest request, AddEquipmentRequest addEquipmentRequest);

    /**
     * 删除设备
     * @param deleteEquipmentRequest
     * @return
     */
    DeleteEquipmentDTO deleteEquipment(DeleteEquipmentRequest  deleteEquipmentRequest);
}
