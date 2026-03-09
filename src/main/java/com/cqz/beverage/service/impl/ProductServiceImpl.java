package com.cqz.beverage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqz.beverage.constant.JwtConstant;
import com.cqz.beverage.exception.BusinessException;
import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.mapper.UserRoleMapper;
import com.cqz.beverage.model.Product;
import com.cqz.beverage.model.User;
import com.cqz.beverage.model.UserRole;
import com.cqz.beverage.model.vo.product.AddProductRequest;
import com.cqz.beverage.model.vo.product.KeyWordProductRequest;
import com.cqz.beverage.model.vo.product.ReviseProductRequest;
import com.cqz.beverage.model.vo.user.PageRequest;
import com.cqz.beverage.service.ProductService;
import com.cqz.beverage.mapper.ProductMapper;
import com.cqz.beverage.service.UserService;
import com.cqz.beverage.utils.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
* @author zhanglinshuai
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2026-03-06 14:45:53
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

    @Resource
    private ProductMapper productMapper;
    @Resource
    private UserService  userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserRoleMapper  userRoleMapper;
    @Override
    public Product addProduct(HttpServletRequest request, AddProductRequest addProductRequest) {
        if (request==null || addProductRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断是否为运营商
        if(!isOperator(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"该用户非运营商无权限");
        }
        //判断商品是否已经存在
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("product_name",addProductRequest.getProductName());
        productQueryWrapper.eq("brand",addProductRequest.getBrand());
        Product product = productMapper.selectOne(productQueryWrapper);
        if(product!=null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_HAS_EXISTS);
        }
        Product addProduct = new Product();
        addProduct.setProductName(addProductRequest.getProductName());
        addProduct.setBrand(addProductRequest.getBrand());
        addProduct.setPrice(addProductRequest.getPrice());
        addProduct.setImage(addProductRequest.getImage());
        addProduct.setDescription(addProductRequest.getDescription());
        addProduct.setProductStatus(0);
        //获取北京时间
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        Date time = instance.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(time);
        try {
            Date date = dateFormat.parse(format);
            addProduct.setCreateTime(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        addProduct.setIsDelete(0);
        addProduct.setProductType(addProductRequest.getProductType());
        productMapper.insert(addProduct);
        return addProduct;
    }

    @Override
    public IPage<Product> getProductsByPage(HttpServletRequest request, PageRequest pageRequest) {
        if (request==null || pageRequest==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        Page<Product> productPage = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        productMapper.selectPage(productPage,null);
        return productPage;
    }

    @Override
    public Product getCurrentProduct(HttpServletRequest request,Long id) {
        if (id==null ||  request==null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        if(!isOperator(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非运营商没有权限");
        }
        Product product = productMapper.selectById(id);
        if(product==null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        return product;
    }

    @Override
    public Product reviseProductInfo(HttpServletRequest request, ReviseProductRequest product) {
        if (request==null || product==null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        //判断是否为运营商
        if(!isOperator(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"用户非运营商无权限");
        }
        Long id = product.getId();
        Product existedProduct = productMapper.selectById(id);
        if(existedProduct==null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        //逐一判断更新的商品信息和原商品信息是否相同
        Integer productStatus = product.getProductStatus();
        String productType = product.getProductType();
        String productName = product.getProductName();
        String brand = product.getBrand();
        String image = product.getImage();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        //商品名称
        if(!StringUtils.isEmpty(productName) && !productName.equals(existedProduct.getProductName())){
            existedProduct.setProductName(productName);
        }
        //商品类别
        if(!StringUtils.isEmpty(productType) && !productType.equals(existedProduct.getProductType())){
            existedProduct.setProductType(productType);
        }
        //商品品牌
        if(!StringUtils.isEmpty(brand) && !brand.equals(existedProduct.getBrand())){
            existedProduct.setBrand(brand);
        }
        //商品图片
        if(!StringUtils.isEmpty(image) && !image.equals(existedProduct.getImage())){
            existedProduct.setImage(image);
        }
        //商品描述
        if(!StringUtils.isEmpty(description) && !description.equals(existedProduct.getDescription())){
            existedProduct.setDescription(description);
        }
        //商品状态
        if(productStatus!=null && !productStatus.equals(existedProduct.getProductStatus())){
            existedProduct.setProductStatus(productStatus);
        }
        //商品价格
        if(price!=null && price.compareTo(existedProduct.getPrice())!=0){
            existedProduct.setPrice(price);
        }
        //更新数据库
        productMapper.updateById(existedProduct);
        return existedProduct;
    }

    @Override
    public boolean deleteProduct(HttpServletRequest request, Long id) {
        if (request==null || id==null){
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXISTS);
        }
        if(!isOperator(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        productMapper.deleteById(id);
        return true;
    }

    @Override
    public IPage<Product> searchProductByKeyword(HttpServletRequest request, PageRequest pageRequest, KeyWordProductRequest keywords) {
        //判断参数是否为空
        if (request==null || pageRequest==null || keywords==null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_EMPTY);
        }
        //判断是否为管理员
        if(!isOperator(request)){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION);
        }
        String productType = keywords.getProductType();
        String productName = keywords.getProductName();
        String brand = keywords.getBrand();
        Integer productStatus = keywords.getProductStatus();
        Page<Product> productPage = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        //组合查询条件
        QueryWrapper<Product> productTypeQueryWrapper = new QueryWrapper<>();
        if(productType!=null){
            productTypeQueryWrapper.like("product_type",productType);
        }
        if(productName!=null){
            productTypeQueryWrapper.like("product_name",productName);
        }
        if(brand!=null){
            productTypeQueryWrapper.like("brand",brand);
        }
        if(productStatus!=null){
            productTypeQueryWrapper.like("product_status",productStatus);
        }
        return productMapper.selectPage(productPage, productTypeQueryWrapper);

    }

    private boolean isOperator(HttpServletRequest request) {
        String header = request.getHeader(JwtConstant.HEADER);
        String token = jwtTokenUtil.getTokenFromHeader(header);
        User currentUser = userService.getCurrentUser(token);
        Long userId = currentUser.getId();
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserRole userRole = userRoleMapper.selectOne(queryWrapper);
        if (userRole==null){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"该用户为普通用户");
        }
        String roleCode = userRole.getRoleCode();
        if (!roleCode.equals("OPERATOR")){
            throw new BusinessException(BusinessExceptionEnum.USER_ROLE_NO_PERMISSION.getCode(),"该用户非运营商无权限");
        }
        return true;
    }
}




