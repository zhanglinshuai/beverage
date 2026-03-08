package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.mapper.UserMapper;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.dto.device.EquipmentDetailDTO;
import com.cqz.beverage.model.dto.device.MotifyEquipmentDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.device.*;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 获取所有的运营商
     * @return
     */
    @GetMapping("/get/allOperators")
    public Map<Long,String> getAllOperators(){
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("role_code","OPERATOR");
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
        Map<Long,String> result = new HashMap<>();
        for(UserRole userRole : userRoles){
            Long userId = userRole.getUserId();
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id",userId);
            User user = userMapper.selectOne(userQueryWrapper);
            if (user!=null){
                result.put(userId,user.getUsername());
            }

        }
        return result;
    }



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
    public Result<DeleteEquipmentDTO>  deleteEquipment(@RequestBody DeleteEquipmentRequest deleteEquipmentRequest, HttpServletRequest request){
        if (deleteEquipmentRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        if (request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        DeleteEquipmentDTO deleteEquipmentDTO = deviceService.deleteEquipment(deleteEquipmentRequest,request);
        if (deleteEquipmentDTO==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),"删除设备失败");
        }
        return Result.success(deleteEquipmentDTO);
    }
    @PutMapping("/motifyDeviceInfo")
    public Result<MotifyEquipmentDTO> motifyEquipment(@RequestBody MotifyEquipmentRequest motifyEquipmentRequest,HttpServletRequest request){
        if (motifyEquipmentRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        if (request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        MotifyEquipmentDTO motifyEquipmentDTO = deviceService.motifyEquipment(motifyEquipmentRequest,request);
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
        IPage<Device> deviceInfoList = deviceService.getDeviceInfoList(servletRequest, pageRequest);
        PageResponseDTO<Device> devicePage = new PageResponseDTO<>();
        //设置配置
        devicePage.setRecords(deviceInfoList.getRecords());
        devicePage.setTotal(deviceInfoList.getTotal());
        devicePage.setPageNum((int) deviceInfoList.getCurrent());
        devicePage.setPageSize((int) deviceInfoList.getSize());
        //转换为分页的响应数据类型
        return Result.success(devicePage);
    }
    @GetMapping("/getDeviceInfo")
    public Result<EquipmentDetailDTO> getDeviceInfo(GetEquipmentInfoRequest getEquipmentInfoRequest, HttpServletRequest request){
        if(getEquipmentInfoRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        if (request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "参数为空");
        }
        EquipmentDetailDTO deviceInfo = deviceService.getDeviceInfo(getEquipmentInfoRequest, request);
        if (deviceInfo==null){
            return Result.fail(BusinessExceptionEnum.DEVICE_NOT_EXISTS.getCode(), "设备不存在");
        }
        return Result.success(deviceInfo);
    }

    @GetMapping("/my/device")
    public Result<PageResponseDTO<Device>> getMyDevice(HttpServletRequest servletRequest, PageRequest pageRequest){
        if (servletRequest==null || pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),"参数为空");
        }
        IPage<Device> myDeviceList = deviceService.getMyDeviceList(servletRequest, pageRequest);
        PageResponseDTO<Device> devicePageResponseDTO = new PageResponseDTO<>();
        devicePageResponseDTO.setRecords(myDeviceList.getRecords());
        devicePageResponseDTO.setTotal(myDeviceList.getTotal());
        devicePageResponseDTO.setPageNum((int) myDeviceList.getCurrent());
        devicePageResponseDTO.setPageSize((int) myDeviceList.getSize());
        return Result.success(devicePageResponseDTO);
    }


    @GetMapping("/search/keyword")
    public Result<PageResponseDTO<Device>> searchDeviceByKeyword(HttpServletRequest servletRequest, SearchDeviceByKeyWordRequest searchDeviceByKeyWordRequest){
        if (searchDeviceByKeyWordRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (servletRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        IPage<Device> deviceIPage = deviceService.searchDeviceByKeyWord(servletRequest, searchDeviceByKeyWordRequest);
        PageResponseDTO<Device> result = new PageResponseDTO<>();
        result.setRecords(deviceIPage.getRecords());
        result.setTotal(deviceIPage.getTotal());
        result.setPageNum((int) deviceIPage.getCurrent());
        result.setPageSize((int) deviceIPage.getSize());
        return Result.success(result);
    }
}
