package com.iweb.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.blog.dao.dos.Archives;
import com.iweb.blog.dao.pojo.Article;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {
    /**
     *
     * @return {@link List }<{@link Archives }>
     */
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
