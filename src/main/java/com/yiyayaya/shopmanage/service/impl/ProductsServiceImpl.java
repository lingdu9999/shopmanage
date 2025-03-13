package com.yiyayaya.shopmanage.service.impl;

import com.yiyayaya.shopmanage.entity.ProductAttributes;
import com.yiyayaya.shopmanage.entity.ProductImages;
import com.yiyayaya.shopmanage.entity.Products;
import com.yiyayaya.shopmanage.entity.Subcategories;
import com.yiyayaya.shopmanage.exception.ServiceException;
import com.yiyayaya.shopmanage.mapper.ProductAttributesMapper;
import com.yiyayaya.shopmanage.mapper.ProductImagesMapper;
import com.yiyayaya.shopmanage.mapper.ProductsMapper;
import com.yiyayaya.shopmanage.mapper.SubcategoriesMapper;
import com.yiyayaya.shopmanage.service.IOrderItemAttributesService;
import com.yiyayaya.shopmanage.service.IProductsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiyayaya.shopmanage.common.Pages;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products> implements IProductsService {

    @Autowired
    private ProductsMapper productsMapper; // 注入 ProductsMapper

    @Autowired
    private SubcategoriesMapper subcategoriesMapper; // 注入 SubcategoriesMapper

    @Autowired
    private ProductImagesMapper productImagesMapper;

    @Autowired
    private ProductAttributesMapper productAttributesMapper;

    @Autowired
    private IOrderItemAttributesService orderItemAttributesService;

    private static final Logger logger = LoggerFactory.getLogger(ProductsServiceImpl.class);

    @Override
    public Pages<Products> getAllProductsWithCategoryNames(Integer pageNum, Integer pageSize, String productName, Integer categoryId, Integer status) {
        Page<Products> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();

        // Add conditions for product name, category ID, and status if provided
        if (productName != null && !productName.isEmpty()) {
            queryWrapper.like("name", productName); // Fuzzy search by product name
        }
        if (categoryId != null) {
            queryWrapper.eq("subcategory_id", categoryId); // Filter by category ID
        }
        if (status != null) {
            queryWrapper.eq("status", status); // Filter by status
        }
        
        // 执行分页查询
        Page<Products> productPage = productsMapper.selectPage(page, queryWrapper);
        
        // 获取分类名称并设置到产品中
        List<Products> productsList = productPage.getRecords();
        for (Products product : productsList) {
            // 根据 subcategoryId 查询分类名称
            String categoryName = getCategoryNameBySubcategoryId(product.getSubcategoryId());
            product.setCategoryName(categoryName);
        }
        
        return new Pages<>(
            productPage.getRecords(),
            productPage.getTotal(),
            productPage.getCurrent(),
            productPage.getSize()
        );
    }

    @Override
    @Transactional
    public boolean addProducts(Products products) {

        try {
            // Generate SKU
            String sku = generateSku(products);
            products.setSku(sku);
            
            // Set default status to "审核状态" (2) for new products
            products.setStatus(2);

            productsMapper.insert(products);
            
            if(products.getImageUrls() != null){
                for (ProductImages productImages : products.getImageUrls()){
                    productImages.setProductId(products.getProductId());
                }
                productImagesMapper.insert(products.getImageUrls());
            }

            List<ProductAttributes> attributesList = new ArrayList<>();
            if(products.getAttributes() != null){
                for (ProductAttributes attributes : products.getAttributes()) {
                    ProductAttributes productAttributes = new ProductAttributes();
                    productAttributes.setProductId(products.getProductId());
                    productAttributes.setName(attributes.getName());
                    productAttributes.setValue(attributes.getValue());
                    attributesList.add(productAttributes);
                }
                productAttributesMapper.insert(attributesList);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error adding product: ", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateProducts(Products products) {
        try {
            // 1. Verify product exists
            Products existingProduct = productsMapper.selectById(products.getProductId());
            if (existingProduct == null) {
                throw new ServiceException("Product with ID " + products.getProductId() + " not found.");
            }

            

            // Check if the status is being changed from "下架 (3)" to "上架 (1)"
            if (existingProduct.getStatus() == 3 && products.getStatus() == 1) {
                // Set status to "审核状态 (2)"
                products.setStatus(2);
            }

            // 2. Update basic product information
            UpdateWrapper<Products> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("product_id", products.getProductId());
            productsMapper.update(products, updateWrapper);

            // 3. Update product images
            // Delete old images
            QueryWrapper<ProductImages> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.eq("product_id", products.getProductId());
            productImagesMapper.delete(imageQueryWrapper);

            // Insert new images
            if(products.getImageUrls() != null){
                for (ProductImages productImage : products.getImageUrls()) {
                    productImage.setProductId(products.getProductId());
                }
                productImagesMapper.insert(products.getImageUrls());
            }


            // 4. Update product attributes
            // Delete old attributes
            QueryWrapper<ProductAttributes> attributeQueryWrapper = new QueryWrapper<>();
            attributeQueryWrapper.eq("product_id", products.getProductId());
            productAttributesMapper.delete(attributeQueryWrapper);

            // Insert new attributes
            List<ProductAttributes> attributesList = new ArrayList<>();
            if(products.getAttributes() != null){
                for (ProductAttributes attribute : products.getAttributes()) {
                    ProductAttributes productAttribute = new ProductAttributes();
                    productAttribute.setProductId(products.getProductId());
                    productAttribute.setName(attribute.getName());
                    productAttribute.setValue(attribute.getValue());
                    attributesList.add(productAttribute);
                }
                productAttributesMapper.insert(attributesList);
            }


            return true;
        } catch (Exception e) {
            logger.error("Error updating product: ", e);
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    @Transactional
    public boolean deleteProduct(List<Integer> ids) {
        try {
            // 1. Get the list of products to be deleted
            QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("product_id", ids);
            List<Products> productsToDelete = productsMapper.selectList(queryWrapper);

            // 2. Iterate through the products and check if they are in "上架 (1)" status
            for (Products product : productsToDelete) {
                if (product.getStatus() == 1) {
                    throw new ServiceException("该产品 " + product.getName() + "已上架，请下架后再进行操作");
                }
                // 3. Check if the product is associated with any orders
                int orderCount = orderItemAttributesService.countOrdersByProductId(product.getProductId());
                if (orderCount > 0) {
                    throw new ServiceException("该产品 " + product.getName() + "已被订单关联，请解除关联后再进行操作");
                }
                
            }

            // 1. Delete product images
            QueryWrapper<ProductImages> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.in("product_id", ids);
            productImagesMapper.delete(imageQueryWrapper);

            // 2. Delete product attributes
            QueryWrapper<ProductAttributes> attributeQueryWrapper = new QueryWrapper<>();
            attributeQueryWrapper.in("product_id", ids);
            
            productAttributesMapper.delete(attributeQueryWrapper);

            // 3. Delete the products
            int result = productsMapper.deleteByIds(ids);

            // Return true if at least one product was successfully deleted
            return result > 0;
        } catch (Exception e) {
            logger.error("Error deleting products: ", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean auditProduct(Integer id, Integer status,Integer type) {
        try {
            // 1. Verify product exists
            Products existingProduct = productsMapper.selectById(id);
            if (existingProduct == null) {
                throw new ServiceException("Product with ID " + id + " not found.");
            }

            // 2. Update product status
            Products product = new Products();
            product.setProductId(id);
            if (status == 1 && type != 2) {
                product.setStatus(2);
            }else {
                product.setStatus(status);
            }
            UpdateWrapper<Products> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("product_id", id);
            int result = productsMapper.update(product, updateWrapper);

            // Return true if the product was successfully updated
            return result > 0;
        } catch (Exception e) {
            logger.error("Error auditing product: ", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private String generateSku(Products products) {
        String categoryCode = getCategoryCode(products.getSubcategoryId()); // Implement this method
        String dateCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomCode = generateRandomString(3); // 3-character random string

        return categoryCode + "-" + dateCode  + "-" + randomCode;
    }

    private String getCategoryCode(Integer subcategoryId) {
        // Implement logic to retrieve category code based on subcategoryId
        // This could involve a database lookup or a configuration mapping
        // Example:
        if (subcategoryId == 1) {
            return "EL"; // Electronics
        } else if (subcategoryId == 2) {
            return "CL"; // Clothing
        } else {
            return "OT"; // Other
        }
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rng = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return sb.toString();
    }

    @Override
    public Products getProductById(Integer id) {
        QueryWrapper<Products> productsQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ProductImages> productImagesQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ProductAttributes> productAttributesQueryWrapper = new QueryWrapper<>();
        productsQueryWrapper.eq("product_id", id);
        productImagesQueryWrapper.eq("product_id", id);
        productAttributesQueryWrapper.eq("product_id", id);

        try{
            List<ProductImages> productImages = productImagesMapper.selectList(productImagesQueryWrapper);
            List<ProductAttributes> productAttributes = productAttributesMapper.selectList(productAttributesQueryWrapper);

            Products products = productsMapper.selectOne(productsQueryWrapper);
            String cateString = getCategoryNameBySubcategoryId(products.getSubcategoryId());
            products.setCategoryName(cateString);
            products.setAttributes(productAttributes);
            products.setImageUrls(productImages);

            return products;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private String getCategoryNameBySubcategoryId(Integer subcategoryId) {
        // 查询子分类名称
        Subcategories subcategory = subcategoriesMapper.selectById(subcategoryId);
        return subcategory != null ? subcategory.getName() : "未知分类"; // 返回分类名称或默认值
    }
}
