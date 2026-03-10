package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.ChangeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.changeRecord.ReviseChangeRecordDTO;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.vo.user.PageRequest;

/**
 * @author zhanglinshuai
 * @description 针对表【change_record(库存变更记录)】的数据库操作Service
 * @createDate 2026-03-10 10:43:55
 */
public interface ChangeRecordService extends IService<ChangeRecord> {
    /**
     * 添加变更记录
     *
     * @param changeRecord
     * @return
     */
    ChangeRecord addRecord(ChangeRecord changeRecord);

    /**
     * 通过设备编号和货道编号获取唯一的库存
     *
     * @param deviceCode
     * @param channelNo
     * @return
     */
    InventoryDTO getInventoryDTO(String deviceCode, String channelNo);

    /**
     * 分页获取变更库存记录
     * @param request
     * @return
     */
    IPage<ChangeRecord> getChangeRecordPage(PageRequest request);

    /**
     * 获取当前的库存记录和库存信息
     * @param changeRecord
     * @return
     */
    ReviseChangeRecordDTO getCurrentRecord(ChangeRecord changeRecord);

    /**
     * 修改库存变更记录
     * @param changeRecord
     * @return
     */
    ReviseChangeRecordDTO reviseChangeRecord(ReviseChangeRecordDTO changeRecord);

}
