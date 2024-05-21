package com.iweb.blog.service;

import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.CommentParam;


public interface CommentsService {
    Result commentByArticleId(Long articleId);

    Result comment(CommentParam commentParam);
}
