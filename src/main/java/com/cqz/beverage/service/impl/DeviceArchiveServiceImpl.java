package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.DeviceArchive;
import com.cqz.beverage.model.Records;
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
import org.springframework.web.bind.annotation.RequestBody;

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
        implements DeviceArchiveService {


    @Resource
    private DeviceArchiveMapper deviceArchiveMapper;

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserService userService;

    @Resource
    private DeviceService deviceService;

    @Override
    public AddMaintenanceDTO addDeviceArchive(AddMaintenanceRequest request) {
        if (request == null) {
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
        if (StrUtil.hasEmpty(deviceCode, operator, manufacturer, maintenanceContent, maintenanceType, remark, warrantyPeriod)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (requestRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (maintenanceTime == null || installDate == null || lastMaintenanceTime == null || productionDate == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (isPermission(requestRequest)) {
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
        } else {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }

    }

    /**
     * 判断是否有权限
     *
     * @param requestRequest
     */
    private boolean isPermission(HttpServletRequest requestRequest) {
        //校验用户是否为管理员或运营商
        String header = requestRequest.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        boolean adminAndOperator = deviceService.isAdminAndOperator(currentUser);
        if (!adminAndOperator) {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        return true;
    }

    @Override
    public List<DeviceArchive> motifyDeviceArchive(MotifyMaintenanceRequest request, HttpServletRequest requestRequest) {
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (requestRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        List<DeviceArchive> result = new ArrayList<>();
        String deviceCode = request.getDeviceCode();
        List<Records> records = request.getRecords();
        String remark = request.getRemark();
        String manufacturer = request.getManufacturer();
        Date productionDate = request.getProductionDate();
        Date installDate = request.getInstallDate();
        Date lastMaintenanceTime = request.getLastMaintenanceTime();
        String warrantyPeriod = request.getWarrantyPeriod();

        //判断参数是否为空
        if (StrUtil.hasEmpty(deviceCode, warrantyPeriod, remark, manufacturer)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (productionDate == null || installDate == null || lastMaintenanceTime == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断设备是否存在
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code", deviceCode);
        Device device = deviceService.getOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        //从数据库中查询已经存在的
        QueryWrapper<DeviceArchive> query = new QueryWrapper<>();
        query.eq("device_code", deviceCode);
        List<DeviceArchive> deviceArchiveList = deviceArchiveMapper.selectList(query);
        if(CollectionUtils.isEmpty(deviceArchiveList)) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_ARCHIVE_NOT_EXIES);
        }
        DeviceArchive existedDeviceArchive = deviceArchiveList.get(0);
        //TODO 基础信息更新的时候也需要更新到数据库当中
        //生产厂家
        if(!manufacturer.equals(existedDeviceArchive.getManufacturer())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setManufacturer(manufacturer);
            }
        }
        //生产日期
        if(!productionDate.equals(existedDeviceArchive.getProductionDate())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setProductionDate(productionDate);
            }
        }
        //安装日期
        if (!installDate.equals(existedDeviceArchive.getInstallDate())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setInstallDate(installDate);
            }
        }
        //最后维修时间
        if(!lastMaintenanceTime.equals(existedDeviceArchive.getLastMaintenanceTime())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setLastMaintenanceTime(lastMaintenanceTime);
            }
        }
        //保修期限
        if (!warrantyPeriod.equals(existedDeviceArchive.getWarrantyPeriod())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setWarrantyPeriod(warrantyPeriod);
            }
        }
        //备注
        if (!remark.equals(existedDeviceArchive.getRemark())) {
            for(DeviceArchive deviceArchive: deviceArchiveList) {
                deviceArchive.setRemark(remark);
            }
        }
        //更新数据库
        for (DeviceArchive deviceArchive: deviceArchiveList) {
            deviceArchiveMapper.updateById(deviceArchive);
        }
        /**
         * 根据设备编号和维护时间作为条件来查询这条维护记录是否存在
         * 如果存在就不插入到deviceArchive表中
         * 如果不存在就插入到deviceArchive当中
         */
        for (Records record : records) {
            Date maintenanceTime = record.getMaintenanceTime();
            QueryWrapper<DeviceArchive> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_code", deviceCode).eq("maintenance_time",maintenanceTime);
            DeviceArchive deviceArchive = deviceArchiveMapper.selectOne(queryWrapper);

            if (deviceArchive == null) {
                DeviceArchive insertArchive = new DeviceArchive();
                insertArchive.setDeviceCode(deviceCode);
                insertArchive.setMaintenanceTime(maintenanceTime);
                insertArchive.setWarrantyPeriod(warrantyPeriod);
                insertArchive.setRemark(remark);
                insertArchive.setInstallDate(installDate);
                insertArchive.setLastMaintenanceTime(lastMaintenanceTime);
                insertArchive.setProductionDate(productionDate);
                insertArchive.setMaintenanceType(record.getMaintenanceType());
                insertArchive.setMaintenanceContent(record.getMaintenanceContent());
                insertArchive.setOperator(record.getOperator());
                insertArchive.setMaintenanceTime(record.getMaintenanceTime());
                deviceArchiveMapper.insert(insertArchive);
                result.add(insertArchive);
            }else {
                //如果相同的话还需要判断维护类型、维护内容、维护人员、维护时间字段是否发生了变化

                deviceArchive.setDeviceCode(deviceCode);
                deviceArchive.setMaintenanceTime(maintenanceTime);
                deviceArchive.setWarrantyPeriod(warrantyPeriod);
                deviceArchive.setRemark(remark);
                deviceArchive.setInstallDate(installDate);
                deviceArchive.setLastMaintenanceTime(lastMaintenanceTime);
                deviceArchive.setProductionDate(productionDate);
                deviceArchive.setMaintenanceType(record.getMaintenanceType());
                deviceArchive.setMaintenanceContent(record.getMaintenanceContent());
                deviceArchive.setOperator(record.getOperator());
                deviceArchive.setMaintenanceTime(record.getMaintenanceTime());
                //
                deviceArchiveMapper.updateById(deviceArchive);
               result.add(deviceArchive);
            }
        }

        return result;
    }

    @Override
    public IPage<DeviceArchive> searchDeviceArchive(PageRequest page, HttpServletRequest request) {
        if (page == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (!isPermission(request)) {
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        //分页搜索
        Page<DeviceArchive> deviceArchivePage = new Page<>(page.getPageNum(), page.getPageSize());
        deviceArchiveMapper.selectPage(deviceArchivePage, null);
        return deviceArchivePage;
    }

    @Override
    public List<DeviceArchive> getCurrentDeviceArchiveInfo(String deviceCode, HttpServletRequest request) {
        if (StrUtil.isBlank(deviceCode)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        QueryWrapper<DeviceArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", deviceCode);
        List<DeviceArchive> deviceArchives = deviceArchiveMapper.selectList(queryWrapper);
        if (deviceArchives == null || deviceArchives.isEmpty()) {
            return new ArrayList<>();
        }
        return deviceArchives;
    }
}




