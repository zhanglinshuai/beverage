package com.cqz.beverage.controller;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.DeviceArchive;
import com.cqz.beverage.model.dto.maintenance.AddMaintenanceDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.maintenance.AddMaintenanceRequest;
import com.cqz.beverage.model.vo.maintenance.MotifyMaintenanceRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceArchiveService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备维修记录
 */
@RestController
@RequestMapping("/maintenance")
public class DeviceMaintenanceController {

    @Resource
    private DeviceArchiveService deviceArchiveService;

    @PostMapping("/add")
    public Result<AddMaintenanceDTO> addDeviceArchive(@RequestBody AddMaintenanceRequest request) {
        if (request == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        AddMaintenanceDTO addMaintenanceDTO = deviceArchiveService.addDeviceArchive(request);
        if (addMaintenanceDTO == null) {
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "添加失败");
        }
        return Result.success(addMaintenanceDTO);
    }

    @PutMapping("/motify")
    public Result<List<DeviceArchive>> motifyDeviceArchive(@RequestBody MotifyMaintenanceRequest request,HttpServletRequest  servletRequest) {
        if (request == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        List<DeviceArchive> deviceArchives = deviceArchiveService.motifyDeviceArchive(request, servletRequest);
        if (CollectionUtils.isEmpty(deviceArchives)) {
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "修改失败");
        }
        return Result.success(deviceArchives);
    }

    @GetMapping("/searchAll")
    public Result<PageResponseDTO<DeviceArchive>> searchAllDeviceArchive(PageRequest page, HttpServletRequest request) {
        if (page == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        if (request == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        IPage<DeviceArchive> deviceArchiveIPage = deviceArchiveService.searchDeviceArchive(page, request);
        PageResponseDTO<DeviceArchive> deviceArchivePageResponseDTO = new PageResponseDTO<>();
        deviceArchivePageResponseDTO.setPageNum(page.getPageNum());
        deviceArchivePageResponseDTO.setPageSize(page.getPageSize());
        deviceArchivePageResponseDTO.setTotal(deviceArchiveIPage.getTotal());
        deviceArchivePageResponseDTO.setRecords(deviceArchiveIPage.getRecords());
        return Result.success(deviceArchivePageResponseDTO);
    }

    @GetMapping("/get/deviceArchive")
    public Result<List<DeviceArchive>> getCurrentDeviceArchive(String deviceCode,HttpServletRequest request) {
        if (deviceCode==null){
            return Result.fail(BusinessExceptionEnum.DEVICE_NOT_EXISTS.getCode(),"设备不存在");
        }
        if (request == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        List<DeviceArchive> currentDeviceArchiveInfo = deviceArchiveService.getCurrentDeviceArchiveInfo(deviceCode, request);
        return Result.success(currentDeviceArchiveInfo);
    }

}
