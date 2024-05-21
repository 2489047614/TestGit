package com.iweb.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.blog.dao.mapper.ArticleBodyMapper;
import com.iweb.blog.dao.mapper.ArticleMapper;
import com.iweb.blog.dao.dos.Archives;
import com.iweb.blog.dao.mapper.ArticleTagMapper;
import com.iweb.blog.dao.pojo.Article;
import com.iweb.blog.dao.pojo.ArticleBody;
import com.iweb.blog.dao.pojo.ArticleTag;
import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.service.*;
import com.iweb.blog.utils.UserThreadLocal;
import com.iweb.blog.vo.*;
import com.iweb.blog.vo.params.ArticleParam;
import com.iweb.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleTagMapper articleTagMapper;


    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }


//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1、分页查询article数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null) {
//            //and category_id=#{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            //加入标签条件查询
//            //article表中并没有tag字段 一篇文章有多个标签
//            //articie_tog article_id 1：n tag_id
//            //我们需要利用一个全新的属于文章标签的queryWrapper将这篇文章的article_Tag查出来，保存到一个list当中。
//            // 然后再根据queryWrapper的in方法选择我们需要的标签即可。
//
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleTags.size() > 0) {
//                // and id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//
//
//        }
//        //是否置顶进行排序,        //时间倒序进行排列相当于order by create_data desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        //分页查询用法 https://blog.csdn.net/weixin_41010294/article/details/105726879
//        List<Article> records = articlePage.getRecords();
//        // 要返回我们定义的vo数据，就是对应的前端数据，不应该只返回现在的数据需要进一步进行处理
//        List<ArticleVo> articleVoList =copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        // 查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //根据浏览数量进行排序
        queryWrapper.orderByDesc(Article::getViewCounts);
        //根据id和标题进行查找
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }


    @Override
    public Result newArticle(int limit) {
        // 查询最新文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //根据创建日期进行排序
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        threadService.updateViewCount(articleMapper,article);
        return copy(article, true, true, true, true);
    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        //注意想要拿到数据必须将接口加入拦截器
        SysUser sysUser = UserThreadLocal.get();
//        System.out.println("sys:"+sysUser.toString());
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        //插入之后 会生成一个文章id（因为新建的文章没有文章id所以要insert一下
        //官网解释："insart后主键会自动'set到实体的ID字段。所以你只需要"getid()就好
//        利用主键自增，mp的insert操作后id值会回到参数对象中
        //https://blog.csdn.net/HSJ0170/article/details/107982866
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                this.articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //插入完之后再给一个id
        article.setBodyId(articleBody.getId());
        //MybatisPlus中的save方法什么时候执行insert，什么时候执行update
        // https://www.cxyzjd.com/article/Horse7/103868144
        //只有当更改数据库时才插入或者更新，一般查询就可以了
        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }


    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }
    private ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


    // 将 Article 对象列表转换为 ArticleVo 对象列表
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            ArticleBodyVo articleBody=findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory){
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }


}