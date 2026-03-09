package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.DeviceArchive;
import com.cqz.beverage.model.dto.maintenance.AddMaintenanceDTO;
import com.cqz.beverage.model.vo.maintenance.AddMaintenanceRequest;
import com.cqz.beverage.model.vo.maintenance.MotifyMaintenanceRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【device_archive(设备档案记录表)】的数据库操作Service
* @createDate 2026-03-07 15:28:35
*/
public interface DeviceArchiveService extends IService<DeviceArchive> {

    /**
     * 设备维修记录录入
     * @param request
     * @return
     */
    AddMaintenanceDTO addDeviceArchive(AddMaintenanceRequest request);

    /**
     * 修改设备维修记录
     * @param request
     * @return
     */
    List<DeviceArchive> motifyDeviceArchive(MotifyMaintenanceRequest request, HttpServletRequest servletRequest);

    /**
     * 查找设备维修记录（分页查询）
     * @param page
     * @param request
     * @return
     */
    IPage<DeviceArchive> searchDeviceArchive(PageRequest page, HttpServletRequest request);

    /**
     * 获取当前设备所有维修档案
     * @param deviceCode
     * @param request
     * @return
     */
    List<DeviceArchive> getCurrentDeviceArchiveInfo(String deviceCode,HttpServletRequest request);
}
