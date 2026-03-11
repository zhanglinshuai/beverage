package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.UserMapper;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.model.WorkOrder;
import com.cqz.beverage.model.dto.workOrder.WorkOrderDTO;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.DeviceService;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.service.WorkOrderService;
import com.cqz.beverage.mapper.WorkOrderMapper;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* @author zhanglinshuai
* @description 针对表【work_order(工单表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:41
*/
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder>
    implements WorkOrderService{

    @Resource
    private  WorkOrderMapper workOrderMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Override
    public WorkOrderDTO addWorkOrder(WorkOrderDTO workOrder, HttpServletRequest request) {
        if (workOrder==null || request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String workType = workOrder.getWorkType();
        String description = workOrder.getDescription();
        String deviceCode = workOrder.getDeviceCode();
        //运营商
        String operatorName = workOrder.getOperatorName();
        //运维人员
        String maintainerName = workOrder.getMaintainerName();
        if(StrUtil.hasBlank(workType,description,deviceCode,operatorName,maintainerName)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断是否有权限创建工单
        boolean adminAndOperator = isAdminAndOperator(request);
        if (!adminAndOperator){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        //自动生成工单编号
        UUID uuid = UUID.randomUUID();
        WorkOrder result = new WorkOrder();
        result.setWorkNo(uuid.toString());
        result.setWorkType(workType);
        result.setWorkStatus("待处理");
        result.setDescription(description);
        //根据设备编号获取设备id
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
        Device device = deviceService.getOne(deviceQueryWrapper);
        result.setDeviceId(device.getId());
        //根据运营商名称获取运营商id
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", operatorName);
        User user = userService.getOne(queryWrapper);
        result.setOperatorId(user.getId());
        //根据运维人员名称获取运维人员id
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>().eq("username", maintainerName);
        User maintainer = userService.getOne(userQueryWrapper);
        result.setMaintainerId(maintainer.getId());
        //插入到数据库当中
        workOrderMapper.insert(result);
        return workOrder;
    }

    private boolean isAdminAndOperator(HttpServletRequest request) {
        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        boolean adminAndOperator = deviceService.isAdminAndOperator(currentUser);
        return adminAndOperator;
    }

    @Override
    public String getOperatorName(String deviceCode) {
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
        Device device = deviceService.getOne(deviceQueryWrapper);
        if (device==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR.getCode(),"根据设备编号找不到该设备");
        }

        Long operationId = device.getOperationId();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id",operationId);
        User user = userService.getOne(userQueryWrapper);
        if (user==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        return user.getUsername();
    }

    @Override
    public List<String> getAllMaintainerName() {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("role_code","MAINTAINER");
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
        List<String> res = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            Long userId = userRole.getUserId();
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", userId);
            User user = userService.getOne(userQueryWrapper);
            res.add(user.getUsername());
        }
        return res;
    }

    @Override
    public IPage<WorkOrderDTO> getWorkOrderDTOPage(HttpServletRequest httpServletRequest, PageRequest pageRequest) {
        if(httpServletRequest==null || pageRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断是否为管理员或运营商
        String header = httpServletRequest.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        //获取当前用户的角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>().eq("user_id", currentUser.getId());
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        String roleCode = userRole.getRoleCode();
        //构造查询条件
        QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
        //如果是运维人员只查询自己负责的工单
        if(roleCode.equals("MAINTAINER")){
            workOrderQueryWrapper.eq("maintainer_id",currentUser.getId());
        }
        //如果是运营商只查询自己设备下的工单
        if(roleCode.equals("OPERATOR")){
            workOrderQueryWrapper.eq("operator_id",currentUser.getId());
        }
        //如果是管理员全量查询
        Page<WorkOrder> workOrderPage = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        workOrderMapper.selectPage(workOrderPage,workOrderQueryWrapper);
        List<WorkOrder> records = workOrderPage.getRecords();
        List<WorkOrderDTO> workOrderDTOs = new ArrayList<>();
        for (WorkOrder workOrder : records) {
            WorkOrderDTO dto = WorkOrderDTO.fromEntity(workOrder);
            getDtoNeedInfo(workOrder, dto);
            workOrderDTOs.add(dto);
        }
        Page<WorkOrderDTO> workOrderDTOPage = new Page<>();
        workOrderDTOPage.setRecords(workOrderDTOs);
        workOrderDTOPage.setTotal(workOrderPage.getTotal());
        workOrderDTOPage.setCurrent(workOrderPage.getCurrent());
        workOrderDTOPage.setSize(workOrderPage.getSize());
        return workOrderDTOPage;
    }

    /**
     * 获取wordOrder中不存在，但是dto中存在的数据
     * @param workOrder
     * @param dto
     */
    private void getDtoNeedInfo(WorkOrder workOrder, WorkOrderDTO dto) {
        //获取设备编号
        Long deviceId = workOrder.getDeviceId();
        Device device = deviceService.getById(deviceId);
        dto.setDeviceCode(device.getDeviceCode());
        //获取运营商名称
        Long operatorId = workOrder.getOperatorId();
        User operator = userService.getById(operatorId);
        dto.setOperatorName(operator.getUsername());
        //获取运维人员名称
        Long maintainerId = workOrder.getMaintainerId();
        User maintainer = userService.getById(maintainerId);
        dto.setMaintainerName(maintainer.getUsername());
    }

    @Override
    public WorkOrderDTO getCurrentWorkOrder(String workNo) {
        if(workNo==null || workNo.isEmpty()){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        QueryWrapper<WorkOrder> wrapper = new QueryWrapper<WorkOrder>().eq("work_no", workNo);
        WorkOrder order = workOrderMapper.selectOne(wrapper);
        WorkOrderDTO dto = WorkOrderDTO.fromEntity(order);
        getDtoNeedInfo(order, dto);
        return dto;
    }

    @Override
    public WorkOrderDTO reviseWorkOrder(WorkOrderDTO workOrder, HttpServletRequest request) {
        if (request==null || workOrder==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //workType、workStatus、finishTime、设备编号可以修改
        String workNo = workOrder.getWorkNo();
        QueryWrapper<WorkOrder> wrapper = new QueryWrapper<WorkOrder>().eq("work_no", workNo);
        WorkOrder existedWorkOrder = workOrderMapper.selectOne(wrapper);
        String workStatus = workOrder.getWorkStatus();
        LocalDateTime finishTime = workOrder.getFinishTime();
        String workType = workOrder.getWorkType();
        String deviceCode = workOrder.getDeviceCode();
        String operatorName = workOrder.getOperatorName();
        String maintainerName = workOrder.getMaintainerName();
        if(workType!=null && !workType.equals(existedWorkOrder.getWorkType())){
            existedWorkOrder.setWorkType(workType);
        }
        if(workStatus!=null && !workStatus.equals(existedWorkOrder.getWorkStatus())){
            if(workStatus.equals("待处理") && existedWorkOrder.getWorkStatus().equals("已完成")){
                throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR.getCode(),"已完成的工单不能变为待处理状态");
            }
            existedWorkOrder.setWorkStatus(workStatus);
        }
        if(finishTime!=null){
            existedWorkOrder.setFinishTime(finishTime);
        }
        Long deviceId = existedWorkOrder.getDeviceId();
        Device device = deviceService.getById(deviceId);
        //如果设备编号变了，运营商跟着变
        if(deviceCode!=null && !deviceCode.equals(device.getDeviceCode())){
            //修改设备编号
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
            Device getOne = deviceService.getOne(deviceQueryWrapper);
            existedWorkOrder.setDeviceId(getOne.getId());
            //修改运营商
            existedWorkOrder.setOperatorId(getOne.getOperationId());
        }
        Long maintainerId = existedWorkOrder.getMaintainerId();
        User maintainer = userService.getById(maintainerId);
        if(maintainerName!=null && maintainerName.equals(maintainer.getUsername())){
            //修改运维人员
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("username",maintainerName);
            User user = userService.getOne(userQueryWrapper);
            existedWorkOrder.setMaintainerId(user.getId());
        }
        //更新数据库信息
        workOrderMapper.updateById(existedWorkOrder);
        WorkOrderDTO newDto = WorkOrderDTO.fromEntity(existedWorkOrder);
        newDto.setDeviceCode(deviceCode);
        newDto.setMaintainerName(maintainerName);
        newDto.setOperatorName(operatorName);
        newDto.setWorkType(workType);
        newDto.setWorkStatus(workStatus);
        newDto.setFinishTime(finishTime);
        return newDto;
    }
}




