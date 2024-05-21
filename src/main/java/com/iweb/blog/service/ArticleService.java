package com.iweb.blog.service;

import com.iweb.blog.vo.ArticleVo;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.ArticleParam;
import com.iweb.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return {@link Result }
     */
    Result listArticle(PageParams pageParams);

    /**
     * 热门文章
     * @param limit
     * @return {@link Result }
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return {@link Result }
     */
    Result newArticle(int limit);

    /**
     * 文章归档
     * @return {@link Result }
     */
    Result listArchives();

    /**
     * 根据id查询文章详情
     * @param id
     * @return {@link ArticleVo }
     */
    ArticleVo findArticleById(Long id);

    Result publish(ArticleParam articleParam);
}
