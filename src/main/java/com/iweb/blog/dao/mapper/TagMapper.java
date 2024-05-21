package com.iweb.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iweb.blog.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
//    List<Tag> findTagByArticleId(Long articleId);
    /**
     * 查询最热的标签前n条
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);

    List<Tag> findTagByArticleId(Long articleId);
}
