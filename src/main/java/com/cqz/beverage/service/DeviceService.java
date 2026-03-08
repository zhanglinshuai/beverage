package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.dto.device.EquipmentDetailDTO;
import com.cqz.beverage.model.dto.device.MotifyEquipmentDTO;
import com.cqz.beverage.model.vo.device.*;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
    DeleteEquipmentDTO deleteEquipment(DeleteEquipmentRequest  deleteEquipmentRequest,HttpServletRequest request);

    /**
     * 修改设备信息
     * @param motifyEquipmentRequest
     * @return
     */
    MotifyEquipmentDTO  motifyEquipment(MotifyEquipmentRequest motifyEquipmentRequest,HttpServletRequest request);

    /**
     * 分页查询
     * @param request
     * @param pageRequest
     * @return
     */
    IPage<Device> getDeviceInfoList(HttpServletRequest request, PageRequest pageRequest);

    /**
     * 查看设备详情
     * @param getEquipmentInfoRequest
     * @return
     */
    EquipmentDetailDTO getDeviceInfo(GetEquipmentInfoRequest getEquipmentInfoRequest, HttpServletRequest request);

    /**
     * 校验用户是否为管理员或运营商
     * @param currentUser
     * @return
     */
    boolean isAdminAndOperator(User currentUser);

    /**
     * 获取我的设备（我是运营商）
     * @param request
     * @param pageRequest
     * @return
     */
    IPage<Device> getMyDeviceList(HttpServletRequest request, PageRequest pageRequest);

    /**
     * 根据关键词来搜索设备（支持设备编号，设备名称）
     * @param request
     * @param searchDeviceByKeyWordRequest
     * @return
     */
    IPage<Device> searchDeviceByKeyWord(HttpServletRequest request, SearchDeviceByKeyWordRequest searchDeviceByKeyWordRequest);
}
