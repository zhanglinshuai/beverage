package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.dto.workOrder.WorkOrderDTO;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.WorkOrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工单接口控制层
 */
@RestController
@RequestMapping("/workOrder")
public class WorkOrderController{

    @Resource
    private WorkOrderService  workOrderService;
    @PostMapping("/create")
    public Result<WorkOrderDTO> createWorkOrder(@RequestBody WorkOrderDTO workOrderDTO, HttpServletRequest request){
        if(workOrderDTO==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        WorkOrderDTO dto = workOrderService.addWorkOrder(workOrderDTO, request);
        if(dto==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(dto);
    }

    @GetMapping("/operator")
    public Result<String> getAllOperator(String deviceCode){
        String operatorName = workOrderService.getOperatorName(deviceCode);
        return Result.success(operatorName);
    }
    @GetMapping("/allMaintainer")
    public Result<List<String>> getAllMaintainer(){
        List<String> list=workOrderService.getAllMaintainerName();
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result<PageResponseDTO<WorkOrderDTO>> getWorkOrderList(HttpServletRequest httpServletRequest, PageRequest pageRequest){
        if(httpServletRequest==null ||pageRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<WorkOrderDTO> workOrderDTOPage = workOrderService.getWorkOrderDTOPage(httpServletRequest, pageRequest);
        PageResponseDTO<WorkOrderDTO> workOrderDTOPageResponseDTO = new PageResponseDTO<>();
        workOrderDTOPageResponseDTO.setTotal(workOrderDTOPage.getTotal());
        workOrderDTOPageResponseDTO.setRecords(workOrderDTOPage.getRecords());
        workOrderDTOPageResponseDTO.setPageNum((int) workOrderDTOPage.getCurrent());
        workOrderDTOPageResponseDTO.setPageSize((int) workOrderDTOPage.getSize());
        return Result.success(workOrderDTOPageResponseDTO);
    }

    @GetMapping("/current")
    public Result<WorkOrderDTO> getCurrentWorkOrder(String workNo){
        if(workNo==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        WorkOrderDTO currentWorkOrder = workOrderService.getCurrentWorkOrder(workNo);
        if (currentWorkOrder==null){
            return Result.fail(BusinessExceptionEnum.PARAM_ERROR.getCode(),  BusinessExceptionEnum.PARAM_ERROR.getMsg());
        }
        return Result.success(currentWorkOrder);
    }

    @PutMapping("/revise")
    public Result<WorkOrderDTO> reviseWorkOrder(@RequestBody WorkOrderDTO workOrder, HttpServletRequest request){
        if (workOrder==null || request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        WorkOrderDTO dto = workOrderService.reviseWorkOrder(workOrder, request);
        if (dto==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(dto);
    }

}
