package com.cqz.beverage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.model.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqz.beverage.model.vo.product.AddProductRequest;
import com.cqz.beverage.model.vo.product.KeyWordProductRequest;
import com.cqz.beverage.model.vo.product.ReviseProductRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zhanglinshuai
* @description 针对表【product(商品表)】的数据库操作Service
* @createDate 2026-03-06 14:45:53
*/
public interface ProductService extends IService<Product> {
    /**
     * 新增商品（只有运营商可以新增）
     * @param request
     * @param addProductRequest
     * @return
     */
    Product addProduct(HttpServletRequest request, AddProductRequest addProductRequest);

    /**
     * 分页查询商品列表
     * @param request
     * @param pageRequest
     * @return
     */
    IPage<Product> getProductsByPage(HttpServletRequest request, PageRequest pageRequest);

    /**
     * 获取编辑的商品信息
     * @param id
     * @param request
     * @return
     */
    Product getCurrentProduct(HttpServletRequest request,Long id);

    /**
     * 编辑商品信息
     * @param request
     * @param product
     * @return
     */
    Product reviseProductInfo(HttpServletRequest request, ReviseProductRequest product);

    /**
     * 删除指定商品信息
     * @param request
     * @param id
     * @return
     */
    boolean deleteProduct(HttpServletRequest request,Long id);

    /**
     * 通过关键词搜索商品
     * @param request
     * @param pageRequest
     * @param keywords
     * @return
     */
    IPage<Product> searchProductByKeyword(HttpServletRequest request, PageRequest pageRequest, KeyWordProductRequest keywords);
}
