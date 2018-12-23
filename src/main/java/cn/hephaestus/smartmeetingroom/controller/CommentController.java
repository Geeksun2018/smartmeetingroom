package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.model.Comment;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import cn.hephaestus.smartmeetingroom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;
    @RequestMapping("/addComment")
    public RetJson addComment(Comment comment, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(comment.getUid() != user.getId()){
            return RetJson.fail(-1,"非法操作");
        }
        if(commentService.insertComment(comment)){
            return RetJson.succcess("commentId",comment.getCommentId());
        }
        return RetJson.fail(-1,"评论失败");
    }

    @RequestMapping("/delComment")
    public RetJson deleteComment(Integer commentId,Integer uid, HttpServletRequest request){
        Comment comment = commentService.getComment(commentId);
        Article article = articleService.getArticle(comment.getArticleId());
        if(uid == comment.getUid()||uid == article.getUserId()){
            commentService.deleteComment(commentId);
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"删除失败！");
    }

    @RequestMapping("/updateComment")
    public RetJson updateComment(Comment comment, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(comment.getUid() != user.getId()){
            return RetJson.fail(-1,"非法操作");
        }
        if(commentService.updateComment(comment)){
            return RetJson.succcess("commentId",comment.getCommentId());
        }
        return RetJson.fail(-1,"非法操作!");
    }

    @RequestMapping("/getComments")
    public RetJson updateComment(Integer articleId, HttpServletRequest request){
        Comment[] comments = commentService.getComments(articleId);
        return RetJson.succcess("comments",comments);
    }
}
