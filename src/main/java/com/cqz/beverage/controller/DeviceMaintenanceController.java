package com.cqz.beverage.controller;

import cn.hutool.db.Page;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备维修记录
 */
@RestController
@RequestMapping("/maintenance")
public class DeviceMaintenanceController {

    @Resource
    private DeviceArchiveService  deviceArchiveService;

    @PostMapping("/add")
    public Result<AddMaintenanceDTO> addDeviceArchive(AddMaintenanceRequest request){
        if (request == null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        AddMaintenanceDTO addMaintenanceDTO = deviceArchiveService.addDeviceArchive(request);
        if (addMaintenanceDTO == null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "添加失败");
        }
        return Result.success(addMaintenanceDTO);
    }
    @PutMapping("/motify")
    public Result<DeviceArchive>  motifyDeviceArchive(MotifyMaintenanceRequest request){
        if (request == null){
            return Result.fail(BusinessExceptionEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        DeviceArchive deviceArchive = deviceArchiveService.motifyDeviceArchive(request);
        if (deviceArchive == null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "修改失败");
        }
        return Result.success(deviceArchive);
    }
    @GetMapping("/searchAll")
    public Result<PageResponseDTO<DeviceArchive>> searchAllDeviceArchive(PageRequest page, HttpServletRequest request){
        if (page == null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        if(request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        List<DeviceArchive> deviceArchives = deviceArchiveService.searchDeviceArchive(page, request);
        PageResponseDTO<DeviceArchive> deviceArchivePageResponseDTO = new PageResponseDTO<>();
        deviceArchivePageResponseDTO.setPageNum(page.getPageNum());
        deviceArchivePageResponseDTO.setPageSize(page.getPageSize());
        deviceArchivePageResponseDTO.setTotal(((long) page.getPageSize() * page.getPageNum()));
        deviceArchivePageResponseDTO.setRecords(deviceArchives);
        return Result.success(deviceArchivePageResponseDTO);
    }
}
