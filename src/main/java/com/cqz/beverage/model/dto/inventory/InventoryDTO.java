package com.cqz.beverage.model.dto.inventory;

import com.cqz.beverage.model.Inventory;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 获取当前库存返回的响应参数
 */
@Data
public class InventoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceCode;
    private String productName;
    private String brand;
    private Integer stock;
    private Integer warningStock;
    private String channelNo;
    private Date updateTime;
    private Integer maxCapacity;

    public static InventoryDTO fromEntity(Inventory inventory) {
        InventoryDTO pageInventoryDTO = new InventoryDTO();
        pageInventoryDTO.setStock(inventory.getStock());
        pageInventoryDTO.setMaxCapacity(inventory.getMaxCapacity());
        pageInventoryDTO.setWarningStock(inventory.getWarningStock());
        pageInventoryDTO.setUpdateTime(inventory.getUpdateTime());
        pageInventoryDTO.setChannelNo(inventory.getChannelNo());
        return  pageInventoryDTO;
    }


}
