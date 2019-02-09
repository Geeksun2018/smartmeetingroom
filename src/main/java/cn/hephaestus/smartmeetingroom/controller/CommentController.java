package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.model.Comment;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import cn.hephaestus.smartmeetingroom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;
    @RequestMapping("/addComment")
    public RetJson addComment(Comment comment, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        comment.setUid(user.getId());
        if(commentService.insertComment(comment)){
            return RetJson.succcess("commentId",comment.getCommentId());
        }
        return RetJson.fail(-1,"评论失败");
    }

    @RequestMapping("/delComment")
    public RetJson deleteComment(Integer commentId, HttpServletRequest request){
        Integer uid = ((User)request.getAttribute("user")).getId();
        Comment comment = commentService.getComment(commentId);
        if(comment == null){
            return RetJson.fail(-1,"该评论不存在！");
        }
        Article article = articleService.getArticle(comment.getArticleId());
        if(uid == comment.getUid()||uid == article.getUserId()){
            commentService.deleteComment(commentId);
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"删除失败！");
    }

    @RequestMapping("/getComments")
    public RetJson updateComment(Integer articleId){
        Comment[] comments = commentService.getComments(articleId);
        return RetJson.succcess("comments",comments);
    }
}
