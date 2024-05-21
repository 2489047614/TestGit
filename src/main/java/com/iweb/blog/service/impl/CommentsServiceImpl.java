package com.iweb.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iweb.blog.dao.mapper.CommentsMapper;
import com.iweb.blog.dao.pojo.Comment;
import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.service.CommentsService;
import com.iweb.blog.service.SysUserService;
import com.iweb.blog.utils.UserThreadLocal;
import com.iweb.blog.vo.CommentVo;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.UserVo;
import com.iweb.blog.vo.params.CommentParam;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论实现类
 * @author DestinyDerek
 * @date 2024/05/17
 */
@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 用于将数据库的评论信息转换为前端展示的评论信息
     * @param comment
     * @return {@link CommentVo }
     */
    public CommentVo copy(Comment comment){
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        Long authorId = comment.getAuthorId();
        UserVo userVo=sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //评论的评论
        List<CommentVo> commentVoList=findCommentByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if (comment.getLevel()>1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //select * from comment where id=#{id} and level=2;
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = this.commentsMapper.selectList(queryWrapper);
        return copyList(comments);
    }

    private List<CommentVo> copyList(List<Comment> commentsList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentsList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    @Override
    public Result commentByArticleId(Long articleId) {
        //查询文章的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        return Result.success(copyList(comments));
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent=commentParam.getParent();
        if (parent==null||parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null?0:parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null?0:toUserId);
        this.commentsMapper.insert(comment);
        return Result.success(null);
    }
}
