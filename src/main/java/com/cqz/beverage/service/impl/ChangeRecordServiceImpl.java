package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.DeviceMapper;
import com.cqz.beverage.mapper.InventoryMapper;
import com.cqz.beverage.mapper.ProductMapper;
import com.cqz.beverage.model.ChangeRecord;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.Inventory;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.model.dto.changeRecord.ReviseChangeRecordDTO;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.ChangeRecordService;
import com.cqz.beverage.mapper.ChangeRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* @author zhanglinshuai
* @description 针对表【change_record(库存变更记录)】的数据库操作Service实现
* @createDate 2026-03-10 10:43:55
*/
@Service
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordMapper, ChangeRecord>
    implements ChangeRecordService{

    @Resource
    private ChangeRecordMapper changeRecordMapper;

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private  ProductMapper productMapper;

    @Override
    public ChangeRecord addRecord(ChangeRecord changeRecord) {
        if(changeRecord==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        Integer changeAmount = changeRecord.getChangeAmount();
        String brand = changeRecord.getBrand();
        String channelNo = changeRecord.getChannelNo();
        String deviceCode = changeRecord.getDeviceCode();
        String operationType = changeRecord.getOperationType();
        String operatorName = changeRecord.getOperatorName();
        String productName = changeRecord.getProductName();

        //判断参数是否为空
        if(StrUtil.hasEmpty(brand,channelNo,deviceCode,operationType,operatorName,productName)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(changeAmount==null || changeAmount<0){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        //当前设备的货道库存变更记录可以存在多个

        //根据操作类型和变更数量来操作库存列表
        // 1. 先获取设备id
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code",deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        //根据设备id和货道编号获取当前设备货道的库存
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<Inventory>().eq("channel_no", channelNo).eq("device_id", device.getId());
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        if(inventory==null){
            throw new BusinessException(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS);
        }
        //获取当前库存
        Integer stock = inventory.getStock();
        //根据操作类型来判断如何操作数据库
        if(operationType.equals("出库")){
            if(stock-changeAmount<0){
                throw new BusinessException(BusinessExceptionEnum.OUT_OF_STOCK);
            }else {
                inventory.setStock(stock-changeAmount);
            }
        } else if(operationType.equals("入库")){
            if(stock+changeAmount>inventory.getMaxCapacity()){
                throw new BusinessException(BusinessExceptionEnum.EXCEEDING_MAX_RANGE);
            }else {
                inventory.setStock(stock+changeAmount);
            }
        }
        //更新货道库存的数据
        inventoryMapper.updateById(inventory);
        //插入数据库
        changeRecordMapper.insert(changeRecord);
        return changeRecord;
    }

    @Override
    public InventoryDTO getInventoryDTO(String deviceCode, String channelNo) {
        if (StrUtil.hasEmpty(deviceCode,channelNo)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code",deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("channel_no",channelNo);
        inventoryQueryWrapper.eq("device_id",device.getId());
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        if (inventory == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS);
        }
        InventoryDTO result = InventoryDTO.fromEntity(inventory);
        result.setDeviceCode(deviceCode);
        //设置商品的信息
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("id",inventory.getProductId());
        Product product = productMapper.selectOne(productQueryWrapper);
        if (product == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        result.setProductName(product.getProductName());
        result.setBrand(product.getBrand());
        return result;
    }

    @Override
    public IPage<ChangeRecord> getChangeRecordPage(PageRequest request) {
        if(request==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        Page<ChangeRecord> changeRecordPage = new Page<>(pageNum, pageSize);
        changeRecordMapper.selectPage(changeRecordPage,null);
        return changeRecordPage;
    }

    @Override
    public ReviseChangeRecordDTO getCurrentRecord(ChangeRecord changeRecord) {
        if(changeRecord==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        String channelNo = changeRecord.getChannelNo();
        String deviceCode = changeRecord.getDeviceCode();
        //获取device_id
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code",deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        //先根据device_id和channel_no获取库存的基本信息
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("channel_no",channelNo);
        inventoryQueryWrapper.eq("device_id",device.getId());
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        if (inventory == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS);
        }
        InventoryDTO inventoryDTO = InventoryDTO.fromEntity(inventory);
        inventoryDTO.setDeviceCode(deviceCode);
        //获取商品的基本信息
        Long productId = inventory.getProductId();
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("id",productId);
        Product product = productMapper.selectOne(productQueryWrapper);
        if (product == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        inventoryDTO.setProductName(product.getProductName());
        inventoryDTO.setBrand(product.getBrand());

        //根据设备编号和货道编号获取当前设备的货道的变更记录
        QueryWrapper<ChangeRecord> changeRecordQueryWrapper = new QueryWrapper<>();
        changeRecordQueryWrapper.eq("device_code",deviceCode);
        changeRecordQueryWrapper.eq("channel_no",channelNo);
        List<ChangeRecord> record = changeRecordMapper.selectList(changeRecordQueryWrapper);
        ReviseChangeRecordDTO result = new ReviseChangeRecordDTO();
        result.setInventoryDTO(inventoryDTO);
        result.setChangeRecords(record);
        return result;
    }

    @Override
    public ReviseChangeRecordDTO reviseChangeRecord(ReviseChangeRecordDTO dto) {
        if(dto==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        List<ChangeRecord> changeRecords = dto.getChangeRecords();
        for(ChangeRecord  changeRecord :changeRecords){
            //只有操作类型和变更数量可以被修改
            String operationType = changeRecord.getOperationType();
            Integer changeAmount = changeRecord.getChangeAmount();
            Long id = changeRecord.getId();
            ChangeRecord existedRecord = changeRecordMapper.selectById(id);
            String existedType = existedRecord.getOperationType();
            Integer existedAmount = existedRecord.getChangeAmount();
            //我现在要先根据设备id和货道编号获取当前库存有多少
            //先通过设备编号获取设备id
            String deviceCode = changeRecord.getDeviceCode();
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<Device>().eq("device_code", deviceCode);
            Device device = deviceMapper.selectOne(deviceQueryWrapper);
            //获取库存
            QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
            inventoryQueryWrapper.eq("device_id",device.getId());
            inventoryQueryWrapper.eq("channel_no",changeRecord.getChannelNo());
            Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);

            //将传入的操作类型和变更数量和数据库中存入的进行比较
            if(!operationType.equals(existedType) || !changeAmount.equals(existedAmount)){
                //当操作类型不变。只有变更数量变化的情况
                if(!changeAmount.equals(existedAmount) && existedType.equals(operationType)){
                    existedRecord.setChangeAmount(changeAmount);
                    //新结果 = 旧结果-旧库存+新库存
                    inventory.setStock(inventory.getStock()-changeAmount);
                    //更新数据库
                    changeRecordMapper.updateById(existedRecord);
                    inventoryMapper.updateById(inventory);
                }
                //那么就需要更新数据库中的数据
                //当操作类型变化，不管变更数量有没有变化的情况
                if(existedType.equals("入库") && !operationType.equals(existedType)){
                    //将入库改为了出库，计算公式   新结果=旧结果-原记录值-新记录值

                    //旧结果
                    Integer OldStock = inventory.getStock();
                    //新结果
                    Integer newStock = OldStock-existedAmount-changeRecord.getChangeAmount();
                    if(newStock<0){
                        throw new BusinessException(BusinessExceptionEnum.UPDATED_STOCK_LESS_THAN_ZERO);
                    }
                    //更新库存数据、库存记录数据
                    inventory.setStock(newStock);
                    changeRecord.setChangeAmount(changeAmount);
                    changeRecord.setOperationType(operationType);
                    //更新时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = dateFormat.format(new Date());
                    try {
                        Date date = dateFormat.parse(format);
                        changeRecord.setChangeTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //更新数据库
                    changeRecordMapper.updateById(changeRecord);
                    inventoryMapper.updateById(inventory);
                }else if(existedType.equals("出库") && !operationType.equals(existedType)){
                    //将出库改为入库   计算公式： 新结果 = 旧结果+原记录值+新记录值
                    //我现在要先根据设备id和货道编号获取当前库存有多少
                    //先通过设备编号获取设备id
                    String code = changeRecord.getDeviceCode();
                    QueryWrapper<Device> queryWrapper = new QueryWrapper<Device>().eq("device_code", code);
                    Device selected = deviceMapper.selectOne(queryWrapper);
                    QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
                    inventoryQueryWrapper.eq("device_id",selected.getId());
                    inventoryQueryWrapper.eq("channel_no",changeRecord.getChannelNo());
                    Inventory res = inventoryMapper.selectOne(wrapper);
                    //旧结果
                    Integer OldStock = res.getStock();
                    //新结果
                    Integer newStock = OldStock+existedAmount+changeRecord.getChangeAmount();
                    if(newStock>res.getMaxCapacity()){
                        throw new BusinessException(BusinessExceptionEnum.UPDATED_STOCK_LESS_THAN_ZERO);
                    }
                    //更新库存数据、库存记录数据
                    res.setStock(newStock);
                    changeRecord.setChangeAmount(changeAmount);
                    changeRecord.setOperationType(operationType);
                    //更新数据库
                    changeRecordMapper.updateById(changeRecord);
                    inventoryMapper.updateById(res);
                }
            }
        }
        return dto;
    }
}




