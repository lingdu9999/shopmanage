package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Pages;
import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.entity.Products;
import com.yiyayaya.shopmanage.entity.UserActivity;
import com.yiyayaya.shopmanage.service.IProductsService;
import com.yiyayaya.shopmanage.service.IUserActivityService;
import com.yiyayaya.shopmanage.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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


    @Autowired
    private IUsersService usersService;

    @Autowired
    private IUserActivityService userActivityService;

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
    public Result<Boolean> addProducts(@RequestBody Products products, @RequestHeader("user") Integer userId) {
        boolean r = productsService.addProducts(products);

        if (r) {
            // 记录用户添加商品行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setUsername(usersService.getUsernameById(userId));
            activity.setAction("添加商品");
            activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 添加了商品: " + products.getName());
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }

        return Result.success(r);
    }

    @PostMapping("/update")
    public Result<Boolean> updateProducts(@RequestBody Products products, @RequestHeader("user") Integer userId) {
        boolean updated = productsService.updateProducts(products);

        if (updated) {
            // 记录用户更新商品行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setUsername(usersService.getUsernameById(userId));
            activity.setAction("更新商品");
            activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 更新了商品SKU为 " + products.getSku() + " 的商品信息");
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }

        return Result.success(updated);
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteProduct(@RequestBody List<Integer> ids, @RequestHeader("user") Integer userId) {
        boolean deleted = productsService.deleteProduct(ids);

        if (deleted) {
            // 记录用户删除商品行为
            for(Integer id : ids) {
                UserActivity activity = new UserActivity();
                activity.setUserId(userId);
                activity.setUsername(usersService.getUsernameById(userId));
                activity.setAction("删除商品");
                activity.setDetails("用户 " + usersService.getUsernameById(userId) + " 删除了商品ID为 " + id + " 的商品");
                activity.setTimestamp(LocalDateTime.now());
                userActivityService.recordActivity(activity);
            }
        }

        return Result.success(deleted);
    }

    @PostMapping("/audit")
    public Result<Boolean> auditProduct(@RequestParam Integer id, @RequestParam Integer status ,@RequestParam(defaultValue = "1", required = false) Integer type, @RequestHeader("user") Integer userId) {
        boolean audited = productsService.auditProduct(id, status,type);
        if (audited) {
            // 记录用户删除商品行为
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setUsername(usersService.getUsernameById(userId));
            activity.setAction( (type == 2 ? "审核" : status == 1 ? "上架" : "下架") +  "商品");
            activity.setDetails("用户 " + usersService.getUsernameById(userId) + (type == 2 ? "审核" : status == 1 ? "上架" : "下架") + " 了商品ID为 " + id + " 的商品");
            
            activity.setTimestamp(LocalDateTime.now());
            userActivityService.recordActivity(activity);
        }
        return Result.success(audited);
    }
}
