package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.service.ProductService;
import com.cqz.beverage.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author zhanglinshuai
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




