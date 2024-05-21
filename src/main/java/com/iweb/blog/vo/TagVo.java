package com.iweb.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVo {

    private Long id;

    private String tagName;

    private String avatar;
}
