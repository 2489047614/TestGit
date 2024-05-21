package com.iweb.blog.controller;

import com.iweb.blog.service.CategoryService;
import com.iweb.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章分类
 * @author DestinyDerek
 * @date 2024/05/19
 */
@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类
     * @return {@link Result }
     */
    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoriesDetailById(@PathVariable("id") Long id){
        return categoryService.categoriesDetailById(id);
    }
}