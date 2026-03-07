package com.cqz.beverage.controller;

import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.vo.device.AddEquipmentRequest;
import com.cqz.beverage.model.vo.device.DeleteEquipmentRequest;
import com.cqz.beverage.service.DeviceService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @PostMapping("/addDevice")
    public Result<AddEquipmentDTO> addEquipment(@RequestBody AddEquipmentRequest addEquipmentRequest, HttpServletRequest servletRequest){
        if (addEquipmentRequest==null || servletRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        AddEquipmentDTO addEquipmentDTO = deviceService.addEquipment(servletRequest, addEquipmentRequest);
        if (addEquipmentDTO==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "添加设备失败");
        }
        return Result.success(addEquipmentDTO);
    }

    @PostMapping("/deleteDevice")
    public Result<DeleteEquipmentDTO>  deleteEquipment(@RequestBody DeleteEquipmentRequest deleteEquipmentRequest){
        if (deleteEquipmentRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        DeleteEquipmentDTO deleteEquipmentDTO = deviceService.deleteEquipment(deleteEquipmentRequest);
        if (deleteEquipmentDTO==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"删除设备失败");
        }
        return Result.success(deleteEquipmentDTO);
    }

}
