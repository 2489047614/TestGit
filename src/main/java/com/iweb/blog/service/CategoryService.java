package com.iweb.blog.service;

import com.iweb.blog.vo.CategoryVo;
import com.iweb.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
