package com.iweb.blog.service;


import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.TagVo;

import java.util.List;

public interface TagService{

    /**
     * 根据文章id查询标签
     * @param
     * @return {@link List }<{@link TagVo }>
     */
    List<TagVo> findTagByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findADetailById(Long id);
}
