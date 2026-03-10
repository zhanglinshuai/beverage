package com.cqz.beverage.model.dto.changeRecord;

import com.cqz.beverage.model.ChangeRecord;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改库存变更记录的响应参数
 */
@Data
public class ReviseChangeRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private InventoryDTO inventoryDTO;
    private List<ChangeRecord> changeRecords;
}
