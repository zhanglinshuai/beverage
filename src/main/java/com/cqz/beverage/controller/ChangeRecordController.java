package com.cqz.beverage.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.ChangeRecord;
import com.cqz.beverage.model.dto.changeRecord.ReviseChangeRecordDTO;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.ChangeRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 库存记录的控制层
 */
@RestController
@RequestMapping("/record")
public class ChangeRecordController {
    @Resource
    private ChangeRecordService changeRecordService;

    @PostMapping("/add")
    public Result<ChangeRecord> addRecord(@RequestBody ChangeRecord changeRecord){
        if(changeRecord==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        ChangeRecord record = changeRecordService.addRecord(changeRecord);
        if (record==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),  BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(record);
    }

    @GetMapping("/product")
    public Result<InventoryDTO> getProductByCodeAndChannel(String deviceCode,String channelNo){
        if(StrUtil.hasEmpty(deviceCode,channelNo)){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),  BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        InventoryDTO dto = changeRecordService.getInventoryDTO(deviceCode, channelNo);
        if(dto==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(),  BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(dto);
    }
    @GetMapping("/page")
    public Result<PageResponseDTO<ChangeRecord>> getChangeRecordPage(PageRequest pageRequest){
        if (pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),  BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<ChangeRecord> changeRecordPage = changeRecordService.getChangeRecordPage(pageRequest);
        PageResponseDTO<ChangeRecord> dto = new PageResponseDTO<>();
        dto.setTotal(changeRecordPage.getTotal());
        dto.setRecords(changeRecordPage.getRecords());
        dto.setPageNum((int) changeRecordPage.getCurrent());
        dto.setPageSize((int) changeRecordPage.getSize());
        return Result.success(dto);
    }

    @GetMapping("/current")
    public Result<ReviseChangeRecordDTO> getCurrentRecord(ChangeRecord changeRecord){
        if (changeRecord==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),   BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        ReviseChangeRecordDTO dto = changeRecordService.getCurrentRecord(changeRecord);
        if (dto==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),   BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        return Result.success(dto);
    }

    @PutMapping("/revise")
    public Result<ReviseChangeRecordDTO> reviseChangeRecord(@RequestBody ReviseChangeRecordDTO reviseChangeRecordDTO){
        if (reviseChangeRecordDTO==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),   BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        ReviseChangeRecordDTO dto = changeRecordService.reviseChangeRecord(reviseChangeRecordDTO);
        if (dto==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(),   BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        return Result.success(dto);
    }

}
