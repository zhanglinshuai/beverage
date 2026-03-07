package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.model.dto.device.AddEquipmentDTO;
import com.cqz.beverage.model.dto.device.DeleteEquipmentDTO;
import com.cqz.beverage.model.dto.device.MotifyEquipmentDTO;
import com.cqz.beverage.model.vo.device.AddEquipmentRequest;
import com.cqz.beverage.model.vo.device.DeleteEquipmentRequest;
import com.cqz.beverage.model.vo.device.GetEquipmentInfoRequest;
import com.cqz.beverage.model.vo.device.MotifyEquipmentRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceService;
import com.cqz.beverage.mapper.DeviceMapper;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【device(设备表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService{

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private UserService  userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserRoleMapper  userRoleMapper;

    @Override
    public AddEquipmentDTO addEquipment(HttpServletRequest request, AddEquipmentRequest addEquipmentRequest) {
        //获取当前登录用户
        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        //判断是否为运营商或管理员
        if(isAdminAndOperator(currentUser)){
            String deviceName = addEquipmentRequest.getDeviceName();
            String deviceCode = addEquipmentRequest.getDeviceCode();
            Long operationId = addEquipmentRequest.getOperationId();
            BigDecimal latitude = addEquipmentRequest.getLatitude();
            BigDecimal longitude = addEquipmentRequest.getLongitude();
            String location = addEquipmentRequest.getLocation();
            String deviceModel = addEquipmentRequest.getDeviceModel();
            String status = addEquipmentRequest.getStatus();
            //判断参数是否为空
            if(StrUtil.hasEmpty(deviceName,deviceCode,location,deviceModel,status)){
                throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
            }
            if(operationId == null){
                throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
            }
            if (latitude == null || longitude == null) {
                throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
            }
            //判断参数是否合规
            if (operationId<0){
                throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR);
            }
            BigDecimal latitudeUpper = new BigDecimal(90);
            BigDecimal latitudeLower = new BigDecimal(-90);
            boolean lessThanUpper = latitude.compareTo(latitudeUpper) < 0;
            boolean MoreThanLower = latitude.compareTo(latitudeLower) > 0;
            if(!lessThanUpper || !MoreThanLower){
                throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR.getCode(),"纬度超出合法范围");
            }
            BigDecimal longitudeUpper = new BigDecimal(180);
            BigDecimal longitudeLower = new BigDecimal(-180);
            boolean lessThanUpperLongitude = latitude.compareTo(longitudeUpper) < 0;
            boolean MoreThanLowerLongitude = latitude.compareTo(longitudeLower) > 0;
            if(!lessThanUpperLongitude || !MoreThanLowerLongitude){
                throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR.getCode(),"经度超出合法范围");
            }
            //查询设备是否已经存在
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.eq("device_code",deviceCode);
            Device device = deviceMapper.selectOne(deviceQueryWrapper);
            if (device != null) {
                throw new BusinessException(BusinessExceptionEnum.DEVICE_ALREADY_EXISTS);
            }
            //不存在，将设备信息保存到数据库当中
            Device addDevice = new Device();
            addDevice.setDeviceName(deviceName);
            addDevice.setDeviceCode(deviceCode);
            addDevice.setLatitude(latitude);
            addDevice.setLongitude(longitude);
            addDevice.setLocation(location);
            addDevice.setDeviceModel(deviceModel);
            addDevice.setStatus(status);
            addDevice.setOperationId(operationId);
            this.save(addDevice);
            return AddEquipmentDTO.fromEntity(addDevice);
        }else {
            throw new  BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非管理员和运营商无操作权限");
        }
    }

    @Override
    public DeleteEquipmentDTO deleteEquipment(DeleteEquipmentRequest deleteEquipmentRequest) {
        //校验参数是否为空
        if(deleteEquipmentRequest == null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String deviceCode = deleteEquipmentRequest.getDeviceCode();
        HttpServletRequest request = deleteEquipmentRequest.getRequest();
        if(StrUtil.isEmpty(deviceCode)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断是否为管理员或者运营商
        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        if(!isAdminAndOperator(currentUser)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"该用户非管理员或运营商无权限");
        }
        //根据设备编号查找设备是否存在
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if(device == null){
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        //如果设备存在的话，把is_delete字段改为1
        device.setIsDelete(1);
        this.updateById(device);
        DeleteEquipmentDTO deleteEquipmentDTO = new DeleteEquipmentDTO();
        deleteEquipmentDTO.setDelete(true);
        deleteEquipmentDTO.setEquipmentCode(device.getDeviceCode());
        return deleteEquipmentDTO;
    }

    @Override
    public MotifyEquipmentDTO motifyEquipment(MotifyEquipmentRequest motifyEquipmentRequest) {
        if(motifyEquipmentRequest == null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String deviceCode = motifyEquipmentRequest.getDeviceCode();
        HttpServletRequest request = motifyEquipmentRequest.getRequest();
        String deviceName = motifyEquipmentRequest.getDeviceName();
        String deviceModel = motifyEquipmentRequest.getDeviceModel();
        String location = motifyEquipmentRequest.getLocation();
        BigDecimal latitude = motifyEquipmentRequest.getLatitude();
        BigDecimal longitude = motifyEquipmentRequest.getLongitude();
        String status = motifyEquipmentRequest.getStatus();
        Long operationId = motifyEquipmentRequest.getOperationId();
        Date installTime = motifyEquipmentRequest.getInstallTime();
        Device oldDevice = motifyEquipmentRequest.getDevice();
        //校验参数是否为空
        if(StrUtil.hasEmpty(deviceCode,deviceName,deviceModel,location,status)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (oldDevice == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        if (installTime == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (operationId == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (latitude == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (longitude == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //校验参数是否合规
        BigDecimal latitudeUpper = new BigDecimal(90);
        BigDecimal latitudeLower = new BigDecimal(-90);
        boolean lessThanUpper = latitude.compareTo(latitudeUpper) < 0;
        boolean MoreThanLower = latitude.compareTo(latitudeLower) > 0;
        if(!lessThanUpper || !MoreThanLower){
            throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR.getCode(),"纬度超出合法范围");
        }
        BigDecimal longitudeUpper = new BigDecimal(180);
        BigDecimal longitudeLower = new BigDecimal(-180);
        boolean lessThanUpperLongitude = latitude.compareTo(longitudeUpper) < 0;
        boolean MoreThanLowerLongitude = latitude.compareTo(longitudeLower) > 0;
        if(!lessThanUpperLongitude || !MoreThanLowerLongitude){
            throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR.getCode(),"经度超出合法范围");
        }
        if (operationId<0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_RANGE_ERROR);
        }
        //判断用户是否为管理员或者运营商
        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        if(!isAdminAndOperator(currentUser)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非管理员或运营商无权限");
        }
        //校验修改后的设备编号是否存在保证唯一性
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code",deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if(device != null){
            throw new BusinessException(BusinessExceptionEnum.DEVICE_ALREADY_EXISTS.getCode(),"设备编号已经存在");
        }
        //给修改前后不同的值更新
        if(!deviceName.equals(oldDevice.getDeviceName())){
            oldDevice.setDeviceName(deviceName);
        }
        if(!deviceModel.equals(oldDevice.getDeviceModel())){
            oldDevice.setDeviceModel(deviceModel);
        }
        if(!location.equals(oldDevice.getLocation())){
            oldDevice.setLocation(location);
        }
        if(!deviceCode.equals(oldDevice.getDeviceCode())){
            oldDevice.setDeviceCode(deviceCode);
        }
        if(!latitude.equals(oldDevice.getLatitude())){
            oldDevice.setLatitude(latitude);
        }
        if(!longitude.equals(oldDevice.getLongitude())){
            oldDevice.setLongitude(longitude);
        }
        if(!status.equals(oldDevice.getStatus())){
            oldDevice.setStatus(status);
        }
        if(!installTime.equals(oldDevice.getInstallTime())){
            oldDevice.setInstallTime(installTime);
        }
        if(!operationId.equals(oldDevice.getOperationId())){
            oldDevice.setOperationId(operationId);
        }
        this.updateById(oldDevice);
        return MotifyEquipmentDTO.fromEntity(oldDevice);

    }

    @Override
    public List<Device> getDeviceInfoList(HttpServletRequest request, PageRequest pageRequest) {
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (pageRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }

        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        if(!isAdminAndOperator(currentUser)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"该用户非管理员或运营商无权限");
        }
        Page<Device> devicePage = new Page<>(pageRequest.getPageNum(),pageRequest.getPageSize());
        Page<Device> selectPage = deviceMapper.selectPage(devicePage, null);
        List<Device> records = selectPage.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return new ArrayList<>();
        }
        return records;
    }

    @Override
    public Device getDeviceInfo(GetEquipmentInfoRequest getEquipmentInfoRequest) {
        if (getEquipmentInfoRequest == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        HttpServletRequest request = getEquipmentInfoRequest.getRequest();
        String deviceCode = getEquipmentInfoRequest.getDeviceCode();
        //校验参数是否为空
        if (StringUtils.isEmpty(deviceCode)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //构造查询条件，查询设备信息
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code",deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if(device != null){
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }

        return device;
    }

    /**
     * 判断是否为运营商或者管理员
     * @param currentUser
     */
    private boolean isAdminAndOperator(User currentUser) {
        if(currentUser == null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_LOGIN);
        }
        //判断是否为管理员或者运营商
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", currentUser.getId());
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        if(userRole == null){
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_FOUND);
        }
        if(userRole.getRoleCode().equals("MAINTAINER")){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非管理员和运营商无操作权限");
        }
        return true;
    }
}




