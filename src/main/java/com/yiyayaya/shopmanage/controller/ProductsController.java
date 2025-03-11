package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Products;
import com.yiyayaya.shopmanage.service.IProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mzy
 * @since 2024-11-21
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private IProductsService productsService;

    @GetMapping("/getAll")
    public Result<Pages<Products>> getAllProductsWithCategoryNames(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(value = "name", required = false) String productName,
            @RequestParam(value = "categoryId",required = false) Integer categoryId,
            @RequestParam(value = "status", required = false) Integer status) {
        Pages<Products> page = productsService.getAllProductsWithCategoryNames(pageNum, pageSize, productName, categoryId, status);
        return Result.success(page);
    }

    @PostMapping("/getProductById")
    public Result<Products> getProductById(@RequestParam Integer id) {
        Products products = productsService.getProductById(id);
        return Result.success(products);
    }

    @PostMapping("/add")
    public Result<Boolean> addProducts(@RequestBody Products products) {
        boolean r = productsService.addProducts(products);
        return Result.success(r);
    }

    @PostMapping("/update")
    public Result<Boolean> updateProducts(@RequestBody Products products) {
        boolean updated = productsService.updateProducts(products);
        return Result.success(updated);
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteProduct(@RequestBody List<Integer> ids) {
        boolean deleted = productsService.deleteProduct(ids);
        return Result.success(deleted);
    }

    @PostMapping("/audit")
    public Result<Boolean> auditProduct(@RequestParam Integer id, @RequestParam Integer status ,@RequestParam(defaultValue = "1", required = false) Integer type) {
        boolean audited = productsService.auditProduct(id, status,type);
        return Result.success(audited);
    }
}
