package com.iweb.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * VO（视图对象）：用于前端展示，
 * 可以屏蔽后端数据结构的变化对前端的影响，
 * 有助于保持前后端的分离和松耦合。
 * @author DestinyDerek
 * @date 2024/05/15
 */
@Data
public class ArticleVo {
    //一定要记得加 要不然 会出现精度损失
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}

