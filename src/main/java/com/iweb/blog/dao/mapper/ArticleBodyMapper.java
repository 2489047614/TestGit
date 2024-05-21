package com.iweb.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iweb.blog.dao.pojo.ArticleBody;
//BaseMapper 接口通常定义了一些基本的数据库操作方法，例如：
//
//insert：插入一条记录到数据库中。
//deleteById：根据主键 ID 删除一条记录。
//updateById：根据主键 ID 更新一条记录。
//selectById：根据主键 ID 查询一条记录。
//selectList：查询所有符合条件的记录列表。
//selectPage：分页查询记录。
public interface ArticleBodyMapper extends BaseMapper<ArticleBody> {
}
