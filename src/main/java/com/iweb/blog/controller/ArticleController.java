package com.iweb.blog.controller;

import com.iweb.blog.common.aop.LogAnnotation;
import com.iweb.blog.common.cache.Cache;
import com.iweb.blog.service.ArticleService;
import com.iweb.blog.vo.ArticleVo;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.ArticleParam;
import com.iweb.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return {@link Result }
     */
    @PostMapping
    //加上此注解，代表要对此接口记录日志
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams){


        return articleService.listArticle(pageParams);
    }

    /**
     * 首页 热门文章
     * @return {@link Result }
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit=5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return {@link Result }
     */
    @PostMapping("new")
    public Result newArticle(){
        int limit=5;
        return articleService.newArticle(limit);
    }

    /**
     * 文章归档
     * @return {@link Result }
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 文章详情 根据id查询
     * @param id
     * @return {@link Result }
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id")Long id){
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }

    //  @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
    //  而最常用的使用请求体传参的无疑是POST请求了，
    //  所以使用@RequestBody接收数据时，一般都用POST方式进行提交。
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        System.out.println("articleParam"+articleParam);
        return articleService.publish(articleParam);
    }

}
