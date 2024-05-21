package com.iweb.blog.controller;

import com.iweb.blog.service.CommentsService;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id")Long articleId){
        return commentsService.commentByArticleId(articleId);
    }
    @GetMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }

}
