package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.dto.device.MotifyEquipmentDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.device.AddEquipmentRequest;
import com.cqz.beverage.model.vo.device.DeleteEquipmentRequest;
import com.cqz.beverage.model.vo.device.GetEquipmentInfoRequest;
import com.cqz.beverage.model.vo.device.MotifyEquipmentRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/motifyDeviceInfo")
    public Result<MotifyEquipmentDTO> motifyEquipment(@RequestBody MotifyEquipmentRequest motifyEquipmentRequest){
        if (motifyEquipmentRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        MotifyEquipmentDTO motifyEquipmentDTO = deviceService.motifyEquipment(motifyEquipmentRequest);
        if (motifyEquipmentDTO==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"修改设备信息失败");
        }
        return Result.success(motifyEquipmentDTO);
    }

    @GetMapping("/getDeviceList")
    public Result<PageResponseDTO<Device>> getDeviceList(HttpServletRequest servletRequest, PageRequest pageRequest){
        if (servletRequest==null ||  pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        List<Device> deviceInfoList = deviceService.getDeviceInfoList(servletRequest, pageRequest);
        Page<Device> devicePage = new Page<>();
        //设置配置
        devicePage.setRecords(deviceInfoList);
        devicePage.setTotal((long) pageRequest.getPageNum() * pageRequest.getPageSize());
        devicePage.setSize(pageRequest.getPageSize());
        devicePage.setCurrent(pageRequest.getPageNum());
        //转换为分页的响应数据类型
        PageResponseDTO<Device> devicePageResponseDTO = PageResponseDTO.buildPageResponseDTO(devicePage);
        return Result.success(devicePageResponseDTO);
    }
    @GetMapping("/getDeviceInfo")
    public Result<Device> getDeviceInfo(GetEquipmentInfoRequest getEquipmentInfoRequest){
        if(getEquipmentInfoRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        Device deviceInfo = deviceService.getDeviceInfo(getEquipmentInfoRequest);
        if (deviceInfo==null){
            return Result.fail(BusinessExceptionEnum.DEVICE_NOT_EXISTS.getCode(), "设备不存在");
        }
        return Result.success(deviceInfo);
    }
}
