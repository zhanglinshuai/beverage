package com.cqz.beverage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.model.dto.user.PageResponseDTO;
import com.cqz.beverage.model.vo.product.AddProductRequest;
import com.cqz.beverage.model.vo.product.KeyWordProductRequest;
import com.cqz.beverage.model.vo.product.ReviseProductRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品接口控制层
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;


    @PostMapping("/add")
    public Result<Product>  addProduct(@RequestBody AddProductRequest addProduct, HttpServletRequest request){
        if (addProduct==null || request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        Product product = productService.addProduct(request, addProduct);
        if (product==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "添加商品失败");
        }
        return Result.success(product);
    }

    @GetMapping("/getProducts")
    public Result<PageResponseDTO<Product>> getProducts(HttpServletRequest request, PageRequest pageRequest){
        if (pageRequest==null ||  request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<Product> productsByPage = productService.getProductsByPage(request, pageRequest);
        PageResponseDTO<Product> dto = new PageResponseDTO<>();
        dto.setTotal(productsByPage.getTotal());
        dto.setRecords(productsByPage.getRecords());
        dto.setPageNum((int) productsByPage.getCurrent());
        dto.setPageSize((int) productsByPage.getSize());
        return Result.success(dto);
    }

    @GetMapping("/currentProduct")
    public Result<Product> getCurrentProduct(HttpServletRequest request,Long id){
        if (request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        Product currentProduct = productService.getCurrentProduct(request, id);
        if (currentProduct==null){
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), BusinessExceptionEnum.SYSTEM_ERROR.getMsg());
        }
        return Result.success(currentProduct);
    }

    @PutMapping("/revise")
    public Result<Product> reviseProduct(@RequestBody ReviseProductRequest reviseProductRequest, HttpServletRequest request){
        if (reviseProductRequest==null || request==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        Product product = productService.reviseProductInfo(request, reviseProductRequest);
        if(product==null){
            return Result.fail(BusinessExceptionEnum.PRODUCT_NOT_EXISTS.getCode(),  BusinessExceptionEnum.PRODUCT_NOT_EXISTS.getMsg());
        }
        return Result.success(product);
    }

    @DeleteMapping("/delete")
    public Result<Boolean> deleteProduct(HttpServletRequest request,Long id){
        if (request==null || id==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        productService.deleteProduct(request, id);
        return Result.success(true);
    }

    @GetMapping("/search/keyword")
    public Result<PageResponseDTO<Product>> searchProductByKeyword(HttpServletRequest request, PageRequest pageRequest, KeyWordProductRequest keyWordProductRequest){
        if (pageRequest==null ||  request==null ||  keyWordProductRequest==null){
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), BusinessExceptionEnum.PARAM_EMPTY.getMsg());
        }
        IPage<Product> productIPage = productService.searchProductByKeyword(request, pageRequest, keyWordProductRequest);
        PageResponseDTO<Product> dto = new PageResponseDTO<>();
        dto.setTotal(productIPage.getTotal());
        dto.setRecords(productIPage.getRecords());
        dto.setPageNum((int) productIPage.getCurrent());
        dto.setPageSize((int) productIPage.getSize());
        return Result.success(dto);
    }
}
