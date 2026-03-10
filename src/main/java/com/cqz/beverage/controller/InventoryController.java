package com.cqz.beverage.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.Inventory;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.dto.inventory.PageInventoryDTO;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.inventory.AddInventoryRequest;
import com.cqz.beverage.model.vo.inventory.DesensitizeDeviceInfo;
import com.cqz.beverage.model.vo.inventory.DesensitizeProductInfo;
import com.cqz.beverage.model.vo.inventory.KeyWordSearchRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.InventoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 库存管理
 */
@RequestMapping("/inventory")
@RestController
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/add")
    public Result<Inventory> addInventory(@RequestBody AddInventoryRequest inventory) {
        if (inventory == null) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        Inventory addedInventory = inventoryService.addInventory(inventory);
        if (addedInventory == null) {
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(addedInventory);
    }

    @GetMapping("/deviceList")
    public Result<List<DesensitizeDeviceInfo>> deviceList() {
        List<DesensitizeDeviceInfo> allDevice = inventoryService.getAllDevice();
        return Result.success(Objects.requireNonNullElseGet(allDevice, ArrayList::new));
    }

    @GetMapping("/productList")
    public Result<List<DesensitizeProductInfo>> productList() {
        List<DesensitizeProductInfo> allProduct = inventoryService.getAllProduct();
        return Result.success(Objects.requireNonNullElseGet(allProduct, ArrayList::new));
    }

    @GetMapping("/getDeviceChannelNo")
    public Result<List<String>> getDeviceChannelNo(String deviceCode) {
        List<String> channelNoByCode = inventoryService.getChannelNoByCode(deviceCode);
        if (channelNoByCode == null || channelNoByCode.isEmpty()) {
            return Result.fail(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS.getCode(), BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS.getMsg());
        }
        return Result.success(channelNoByCode);
    }

    @GetMapping("/page")
    public Result<PageResponseDTO<PageInventoryDTO>> inventoryList(PageRequest pageRequest) {
        if(pageRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<PageInventoryDTO> inventoryByPage = inventoryService.getInventoryByPage(pageRequest);
        PageResponseDTO<PageInventoryDTO> responseDTO = new PageResponseDTO<>();
        responseDTO.setTotal(inventoryByPage.getTotal());
        responseDTO.setRecords(inventoryByPage.getRecords());
        responseDTO.setPageNum((int)inventoryByPage.getCurrent());
        responseDTO.setPageSize((int)inventoryByPage.getSize());
        return Result.success(responseDTO);
    }

    @GetMapping("/keyword/page")
    public Result<PageResponseDTO<PageInventoryDTO>>  inventoryKeywordPage(PageRequest pageRequest, KeyWordSearchRequest keyWordSearchRequest) {
        if(pageRequest==null || keyWordSearchRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_ERROR.getCode(),  BusinessExceptionEnum.PARAM_ERROR.getMsg());
        }
        IPage<PageInventoryDTO> inventoryPageByKeyWord = inventoryService.getInventoryPageByKeyWord(keyWordSearchRequest, pageRequest);
        PageResponseDTO<PageInventoryDTO> responseDTO = new PageResponseDTO<>();
        responseDTO.setTotal(inventoryPageByKeyWord.getTotal());
        responseDTO.setRecords(inventoryPageByKeyWord.getRecords());
        responseDTO.setPageNum((int)inventoryPageByKeyWord.getCurrent());
        responseDTO.setPageSize((int)inventoryPageByKeyWord.getSize());
        return Result.success(responseDTO);
    }


    @GetMapping("/current")
    public Result<InventoryDTO> getCurrentInventory(String deviceCode,String channelNo) {
        if (StrUtil.hasEmpty(deviceCode,channelNo)) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        InventoryDTO inventoryDTO = inventoryService.getCurrentInventory(channelNo,deviceCode);
        return Result.success(inventoryDTO);
    }

}
