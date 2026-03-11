package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.WorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.workOrder.WorkOrderDTO;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;

import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【work_order(工单表)】的数据库操作Service
* @createDate 2026-03-06 14:45:41
*/
public interface WorkOrderService extends IService<WorkOrder> {
    /**
     * 创建工单(管理员）
     * @param workOrder
     * @return
     */
    WorkOrderDTO addWorkOrder(WorkOrderDTO workOrder, HttpServletRequest request);

    /**
     * 获取当前设备运营商名称
     * @return
     */
    String getOperatorName(String deviceCode);

    /**
     * 获取所有运维人员名称
     * @return
     */
    List<String> getAllMaintainerName();

    /**
     * 分页获取工单列表
     * @param httpServletRequest
     * @param pageRequest
     * @return
     */
    IPage<WorkOrderDTO> getWorkOrderDTOPage(HttpServletRequest httpServletRequest, PageRequest pageRequest);

    /**
     * 根据工单编号获取工单信息
     * @param workNo
     * @return
     */
    WorkOrderDTO getCurrentWorkOrder(String workNo);

    /**
     * 编辑当前工单
     * @param workOrder
     * @param request
     * @return
     */
    WorkOrderDTO reviseWorkOrder(WorkOrderDTO workOrder, HttpServletRequest request);
}
