package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.model.Comment;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import cn.hephaestus.smartmeetingroom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;

    User user=null;
    UserInfo userInfo=null;
    @ModelAttribute
    public void common(HttpServletRequest request){
        if (user==null){
            user = (User) request.getAttribute("user");
            userInfo=(UserInfo)request.getAttribute("userInfo");
        }
        return;
    }
    @RequestMapping("/addComment")
    public RetJson addComment( Comment comment){
        comment.setUid(user.getId());
        comment.setCreatTime(new Date());
        if(commentService.insertComment(comment)){
            return RetJson.succcess("commentId",comment.getCommentId());
        }
        return RetJson.fail(-1,"评论失败");
    }

    @RequestMapping("/delComment")
    public RetJson deleteComment(Integer commentId){
        Integer uid = user.getId();
        Integer oid=userInfo.getOid();

        Comment comment = commentService.getComment(commentId);
        if(comment == null){
            return RetJson.fail(-1,"该评论不存在！");
        }
        Article article = articleService.getArticle(comment.getArticleId(),oid);
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
