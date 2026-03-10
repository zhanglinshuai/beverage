package com.cqz.beverage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.DeviceChannelMapper;
import com.cqz.beverage.mapper.DeviceMapper;
import com.cqz.beverage.mapper.ProductMapper;
import com.cqz.beverage.model.Device;
import com.cqz.beverage.model.DeviceChannel;
import com.cqz.beverage.model.Inventory;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.model.dto.inventory.InventoryDTO;
import com.cqz.beverage.model.dto.inventory.PageInventoryDTO;
import com.cqz.beverage.model.vo.inventory.AddInventoryRequest;
import com.cqz.beverage.model.vo.inventory.DesensitizeDeviceInfo;
import com.cqz.beverage.model.vo.inventory.DesensitizeProductInfo;
import com.cqz.beverage.model.vo.inventory.KeyWordSearchRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.InventoryService;
import com.cqz.beverage.mapper.InventoryMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhanglinshuai
 * @description 针对表【inventory(库存表-存储各个设备的各个商品还有多少库存)】的数据库操作Service实现
 * @createDate 2026-03-06 14:45:53
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
        implements InventoryService {

    @Resource
    private InventoryMapper inventoryMapper;

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ProductMapper productMapper;

    @Resource
    private DeviceChannelMapper deviceChannelMapper;

    @Override
    public Inventory addInventory(AddInventoryRequest inventory) {

        if (inventory == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        Integer warningStock = inventory.getWarningStock();
        String channelNo = inventory.getChannelNo();
        Integer stock = inventory.getStock();
        String productName = inventory.getProductName();
        Integer maxCapacity = inventory.getMaxCapacity();
        String brand = inventory.getBrand();
        String deviceCode = inventory.getDeviceCode();
        //判断参数是否为空
        if (StrUtil.hasEmpty(channelNo, productName, brand, deviceCode)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (warningStock == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (stock == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if (maxCapacity == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        if(stock>maxCapacity){
            throw new BusinessException(BusinessExceptionEnum.STOCK_MUST_LESS_MAX_CAPACITY);
        }
        //获取设备信息
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code", deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        //根据商品品牌和商品名称来获取商品信息
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>()
                .eq("product_name", productName)
                .eq("brand", brand);
        Product product = productMapper.selectOne(productQueryWrapper);
        //根据设备信息和货道编号判断货道库存配置是否存在
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("device_id", device.getId());
        inventoryQueryWrapper.eq("channel_no", channelNo);
        Long count = inventoryMapper.selectCount(inventoryQueryWrapper);
        if (count > 0) {
            throw new BusinessException(BusinessExceptionEnum.CAPACITY_HAS_EXISTS);
        }
        //不存在,将数据插入到数据库当中
        Inventory res = new Inventory();
        res.setMaxCapacity(maxCapacity);
        res.setWarningStock(warningStock);
        res.setStock(stock);
        res.setDeviceId(device.getId());
        res.setProductId(product.getId());
        res.setChannelNo(channelNo);
        this.save(res);
        return res;
    }

    @Override
    public List<DesensitizeDeviceInfo> getAllDevice() {
        List<DesensitizeDeviceInfo> deviceInfos = new ArrayList<>();
        List<Device> devices = deviceMapper.selectList(null);
        for (Device device : devices) {
            DesensitizeDeviceInfo desensitizeDeviceInfo = new DesensitizeDeviceInfo();
            desensitizeDeviceInfo.setDeviceName(device.getDeviceName());
            desensitizeDeviceInfo.setDeviceCode(device.getDeviceCode());
            deviceInfos.add(desensitizeDeviceInfo);
        }
        return deviceInfos;
    }

    @Override
    public List<DesensitizeProductInfo> getAllProduct() {
        List<DesensitizeProductInfo> desensitizeProductInfos = new ArrayList<>();
        List<Product> products = productMapper.selectList(null);
        for (Product product : products) {
            DesensitizeProductInfo desensitizeProductInfo = new DesensitizeProductInfo();
            desensitizeProductInfo.setProductName(product.getProductName());
            desensitizeProductInfo.setBrand(product.getBrand());
            desensitizeProductInfos.add(desensitizeProductInfo);
        }
        return desensitizeProductInfos;
    }

    @Override
    public List<String> getChannelNoByCode(String deviceCode) {
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code", deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        if (device == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        QueryWrapper<DeviceChannel> deviceChannelQueryWrapper = new QueryWrapper<>();
        deviceChannelQueryWrapper.eq("device_id", device.getId());
        List<DeviceChannel> deviceChannels = deviceChannelMapper.selectList(deviceChannelQueryWrapper);
        List<String> channelNos = new ArrayList<>();
        for (DeviceChannel deviceChannel : deviceChannels) {
            channelNos.add(deviceChannel.getChannelNo());
        }
        return channelNos;
    }

    @Override
    public IPage<PageInventoryDTO> getInventoryByPage(PageRequest pageRequest) {
        Page<Inventory> inventoryPage = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        inventoryMapper.selectPage(inventoryPage, null);
        Page<PageInventoryDTO> dtoPage = new Page<>();
        dtoPage.setTotal(inventoryPage.getTotal());
        dtoPage.setSize(inventoryPage.getSize());
        dtoPage.setCurrent(inventoryPage.getCurrent());
        List<Inventory> records = inventoryPage.getRecords();
        List<PageInventoryDTO> inventoryDTOList = new ArrayList<>();
        for (Inventory inventory : records) {
            Long deviceId = inventory.getDeviceId();
            Long productId = inventory.getProductId();
            //获取设备的名称
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.eq("id", deviceId);
            Device device = deviceMapper.selectOne(deviceQueryWrapper);
            String deviceCode = device.getDeviceCode();
            //获取商品的名称和品牌
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("id", productId);
            Product product = productMapper.selectOne(productQueryWrapper);
            String productName = product.getProductName();
            String brand = product.getBrand();
            //创建PageInventoryDTO并插入到list当中
            PageInventoryDTO pageInventoryDTO = PageInventoryDTO.fromEntity(inventory);
            pageInventoryDTO.setProductName(productName);
            pageInventoryDTO.setBrand(brand);
            pageInventoryDTO.setDeviceCode(deviceCode);
            inventoryDTOList.add(pageInventoryDTO);
        }
        dtoPage.setRecords(inventoryDTOList);
        return dtoPage;
    }

    @Override
    public IPage<PageInventoryDTO> getInventoryPageByKeyWord(KeyWordSearchRequest keyWordSearchRequest, PageRequest request) {
        if (keyWordSearchRequest == null || request == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        String brand = keyWordSearchRequest.getBrand();
        String deviceCode = keyWordSearchRequest.getDeviceCode();
        String productName = keyWordSearchRequest.getProductName();
        //判断参数，保证安全性
        if (brand != null && productName == null) {
            throw new BusinessException(BusinessExceptionEnum.BRAND_AND_PRODUCT_MUST_EXIST);
        }
        if(brand==null && productName!=null){
            throw new BusinessException(BusinessExceptionEnum.BRAND_MUST_EXIST);
        }
        Page<Inventory> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        //通过deviceCode找到设备
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", deviceCode);
        Device selectOne = deviceMapper.selectOne(queryWrapper);
        if (selectOne == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS);
        }
        if (!Objects.equals(selectOne.getStatus(), "ONLINE")) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_UNAVAILABLE);
        }
        //通过brand和ProductName找到商品
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("brand", brand);
        wrapper.eq("product_name", productName);
        Product selected = productMapper.selectOne(wrapper);
        if (selected == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXIST_IN_CHANNEL);
        }

        //组合查询条件
        if (brand != null) {
            inventoryQueryWrapper.like("brand", brand);
        }
        if (deviceCode != null) {
            inventoryQueryWrapper.like("device_id", selectOne.getId());
        }
        if (productName != null) {
            inventoryQueryWrapper.like("product_id", selected.getId());
        }
        inventoryMapper.selectPage(page, inventoryQueryWrapper);
        //inventory转换为PageInventoryDTO
        Page<PageInventoryDTO> dtoPage = new Page<>();
        dtoPage.setTotal(page.getTotal());
        dtoPage.setSize(page.getSize());
        dtoPage.setCurrent(page.getCurrent());
        List<PageInventoryDTO> inventoryDTOList = new ArrayList<>();
        for (Inventory inventory : page.getRecords()) {
            Long deviceId = inventory.getDeviceId();
            Long productId = inventory.getProductId();
            //获取设备的编号
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.eq("id", deviceId);
            Device device = deviceMapper.selectOne(deviceQueryWrapper);
            String code = device.getDeviceCode();
            //获取商品的名称和品牌
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("id", productId);
            Product product = productMapper.selectOne(productQueryWrapper);
            String name = product.getProductName();
            String productBrand = product.getBrand();
            //创建PageInventoryDTO并插入到list当中
            PageInventoryDTO pageInventoryDTO = PageInventoryDTO.fromEntity(inventory);
            pageInventoryDTO.setProductName(name);
            pageInventoryDTO.setBrand(productBrand);
            pageInventoryDTO.setDeviceCode(code);
            inventoryDTOList.add(pageInventoryDTO);
        }
        dtoPage.setRecords(inventoryDTOList);

        return dtoPage;
    }

    @Override
    public InventoryDTO getCurrentInventory(String channelNo, String deviceCode) {
        if (deviceCode == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_NOT_EXISTS);
        }
        if(channelNo==null){
            throw new BusinessException(BusinessExceptionEnum.CHANNEL_NOT_EXISTS);
        }
        QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
        deviceQueryWrapper.eq("device_code", deviceCode);
        Device device = deviceMapper.selectOne(deviceQueryWrapper);
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("device_id", device.getId());
        inventoryQueryWrapper.eq("channel_no", channelNo);
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        if (inventory == null) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_CHANNEL_NOT_EXISTS);
        }
        InventoryDTO result = InventoryDTO.fromEntity(inventory);
        result.setDeviceCode(device.getDeviceCode());
        //查询出product的信息
        Long productId = inventory.getProductId();
        Product product = productMapper.selectById(productId);
        result.setProductName(product.getProductName());
        result.setBrand(product.getBrand());
        return result;
    }


}




