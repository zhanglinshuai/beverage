package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.DeviceChannel;
import com.cqz.beverage.model.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.dto.inventory.PageInventoryDTO;
import com.cqz.beverage.model.vo.inventory.AddInventoryRequest;
import com.cqz.beverage.model.vo.inventory.DesensitizeDeviceInfo;
import com.cqz.beverage.model.vo.inventory.DesensitizeProductInfo;
import com.cqz.beverage.model.vo.inventory.KeyWordSearchRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【inventory(库存表-每台设备的各货道商品的库存情况)】的数据库操作Service
* @createDate 2026-03-06 14:45:53
*/
public interface InventoryService extends IService<Inventory> {

    /**
     * 添加库存配置
     * @param inventory
     * @return
     */
    Inventory addInventory(AddInventoryRequest inventory);

    /**
     * 获取所有的脱敏后的设备信息
     * @return
     */
    List<DesensitizeDeviceInfo> getAllDevice();

    /**
     * 获取所有脱敏后的商品信息
     * @return
     */
    List<DesensitizeProductInfo> getAllProduct();

    /**
     * 通过设备编号获取当前设备下的所有货道编号
     * @param deviceCode
     * @return
     */
    List<String> getChannelNoByCode(String deviceCode);

    /**
     * 分页获取库存配置
     * @param pageRequest
     * @return
     */
    IPage<PageInventoryDTO> getInventoryByPage(PageRequest pageRequest);

    /**
     * 通过关键词搜索进行分页查询
     * @param keyWordSearchRequest
     * @param request
     * @return
     */
    IPage<PageInventoryDTO> getInventoryPageByKeyWord(KeyWordSearchRequest  keyWordSearchRequest,PageRequest request);

    /**
     * 根据货道编号获取当前的货道库存信息
     * @param channelNo
     * @param deviceCode
     * @return
     */
    InventoryDTO getCurrentInventory(String channelNo,String deviceCode);

    /**
     * 修改当前的货道库存配置
     * @param inventoryDTO
     * @return
     */
    InventoryDTO reviseInventory(InventoryDTO inventoryDTO,HttpServletRequest request);

}
