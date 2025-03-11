package com.yiyayaya.shopmanage.service;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.entity.Products;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
public interface IProductsService extends IService<Products> {
    Pages<Products> getAllProductsWithCategoryNames(Integer pageNum, Integer pageSize, String productName, Integer categoryId, Integer status);
    boolean addProducts(Products products);
    Products getProductById(Integer id);
    boolean updateProducts(Products products); // Add the updateProducts method
    boolean deleteProduct(List<Integer> ids); // Modified to accept a list of IDs
    boolean auditProduct(Integer id, Integer status,Integer type); // Add the auditProduct method
}
