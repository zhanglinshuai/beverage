package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.model.DeviceArchive;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.dto.maintenance.AddMaintenanceDTO;
import com.cqz.beverage.model.vo.maintenance.AddMaintenanceRequest;
import com.cqz.beverage.model.vo.maintenance.MotifyMaintenanceRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceArchiveService;
import com.cqz.beverage.mapper.DeviceArchiveMapper;
import com.cqz.beverage.service.DeviceService;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【device_archive(设备档案记录表)】的数据库操作Service实现
* @createDate 2026-03-07 15:28:35
*/
@Service
public class DeviceArchiveServiceImpl extends ServiceImpl<DeviceArchiveMapper, DeviceArchive>
    implements DeviceArchiveService{


    @Resource
    private DeviceArchiveMapper deviceArchiveMapper;

    @Resource
    private JwtTokenUtil  jwtTokenUtil;
    @Resource
    private UserService userService;

    @Resource
    private DeviceService deviceService;
    @Override
    public AddMaintenanceDTO addDeviceArchive(AddMaintenanceRequest request) {
        if(request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        HttpServletRequest requestRequest = request.getRequest();
        String deviceCode = request.getDeviceCode();
        String operator = request.getOperator();
        String manufacturer = request.getManufacturer();
        String maintenanceType = request.getMaintenanceType();
        String remark = request.getRemark();
        String warrantyPeriod = request.getWarrantyPeriod();
        Date maintenanceTime = request.getMaintenanceTime();
        String maintenanceContent = request.getMaintenanceContent();
        Date installDate = request.getInstallDate();
        Date lastMaintenanceTime = request.getLastMaintenanceTime();
        Date productionDate = request.getProductionDate();
        //校验参数是否为空
        if(StrUtil.hasEmpty(deviceCode,operator,manufacturer,maintenanceContent,maintenanceType,remark,warrantyPeriod)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(requestRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(maintenanceTime==null || installDate==null||lastMaintenanceTime==null||productionDate==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(isPermission(requestRequest)){
            DeviceArchive deviceArchive = new DeviceArchive();
            deviceArchive.setDeviceCode(deviceCode);
            deviceArchive.setOperator(operator);
            deviceArchive.setManufacturer(manufacturer);
            deviceArchive.setMaintenanceType(maintenanceType);
            deviceArchive.setRemark(remark);
            deviceArchive.setWarrantyPeriod(warrantyPeriod);
            deviceArchive.setMaintenanceTime(maintenanceTime);
            deviceArchive.setInstallDate(installDate);
            deviceArchive.setLastMaintenanceTime(lastMaintenanceTime);
            deviceArchive.setProductionDate(productionDate);
            deviceArchive.setMaintenanceContent(maintenanceContent);
            deviceArchiveMapper.insert(deviceArchive);
            return AddMaintenanceDTO.fromEntity(deviceArchive);
        }else {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }

    }

    /**
     * 判断是否有权限
     * @param requestRequest
     */
    private boolean isPermission(HttpServletRequest requestRequest) {
        //校验用户是否为管理员或运营商
        String header = requestRequest.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        boolean adminAndOperator = deviceService.isAdminAndOperator(currentUser);
        if(!adminAndOperator){
            throw new  BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        return true;
    }

    @Override
    public DeviceArchive motifyDeviceArchive(MotifyMaintenanceRequest request) {
        if(request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        HttpServletRequest requestRequest = request.getRequest();
        DeviceArchive deviceArchive = request.getDeviceArchive();
        String deviceCode = request.getDeviceCode();
        String operator = request.getOperator();
        String manufacturer = request.getManufacturer();
        String maintenanceType = request.getMaintenanceType();
        String remark = request.getRemark();
        String warrantyPeriod = request.getWarrantyPeriod();
        Date maintenanceTime = request.getMaintenanceTime();
        String maintenanceContent = request.getMaintenanceContent();
        Date installDate = request.getInstallDate();
        Date lastMaintenanceTime = request.getLastMaintenanceTime();
        Date productionDate = request.getProductionDate();
        //判断参数是否为空
        if (deviceArchive == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        if (StrUtil.hasEmpty(deviceCode,operator,manufacturer,maintenanceContent,maintenanceType,remark,warrantyPeriod)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(maintenanceTime==null||installDate==null||lastMaintenanceTime==null||productionDate==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //查询原设备编号和要修改的设备编号是否相同
        if(!deviceArchive.getDeviceCode().equals(deviceCode)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR.getCode(),"修改设备错误");
        }
        //判断是否为管理员或运营商
        if(isPermission(requestRequest)){
            //更新修改之后的值
            if(!operator.equals(deviceArchive.getOperator())){
                deviceArchive.setOperator(operator);
            }
            if(!manufacturer.equals(deviceArchive.getManufacturer())){
                deviceArchive.setManufacturer(manufacturer);
            }
            if(!maintenanceType.equals(deviceArchive.getMaintenanceType())){
                deviceArchive.setMaintenanceType(maintenanceType);
            }
            if(!remark.equals(deviceArchive.getRemark())){
                deviceArchive.setRemark(remark);
            }
            if(!warrantyPeriod.equals(deviceArchive.getWarrantyPeriod())){
                deviceArchive.setWarrantyPeriod(warrantyPeriod);
            }
            if(!installDate.equals(deviceArchive.getInstallDate())){
                deviceArchive.setInstallDate(installDate);
            }
            if (!lastMaintenanceTime.equals(deviceArchive.getLastMaintenanceTime())) {
                deviceArchive.setLastMaintenanceTime(lastMaintenanceTime);
            }
            if (!productionDate.equals(deviceArchive.getProductionDate())) {
                deviceArchive.setProductionDate(productionDate);
            }
            if (!maintenanceContent.equals(deviceArchive.getMaintenanceContent())) {
                deviceArchive.setMaintenanceContent(maintenanceContent);
            }
            if(!maintenanceTime.equals(deviceArchive.getMaintenanceTime())){
                deviceArchive.setMaintenanceTime(maintenanceTime);
            }
            //数据库中更新
            deviceArchiveMapper.updateById(deviceArchive);
            return deviceArchive;
        }else {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
    }

    @Override
    public List<DeviceArchive> searchDeviceArchive(PageRequest page, HttpServletRequest request) {
        if (page == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(!isPermission(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        //分页搜索
        Page<DeviceArchive> deviceArchivePage = new Page<>(page.getPageNum(), page.getPageSize());
        Page<DeviceArchive> selectPage = deviceArchiveMapper.selectPage(deviceArchivePage, null);
        List<DeviceArchive> records = selectPage.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return new ArrayList<>();
        }
        return records;
    }
}




