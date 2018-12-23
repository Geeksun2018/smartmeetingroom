package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @RequestMapping("/uploadArticle")
    public RetJson uploadArticle(Article article, HttpServletRequest request){
        //校验参数！
        User user = (User) request.getAttribute("user");
        if(user.getId() == article.getUserId()) {
            return RetJson.fail(-1,"非法操作");
        }
        if(articleService.insertArticle(article))
            return RetJson.succcess("articleId",article.getArticleId());
        return RetJson.fail(-1,"插入失败！");
    }

    @RequestMapping("/updateArticle")
    public RetJson updateArticle(Article article, HttpServletRequest request){
        //校验参数！
        User user = (User) request.getAttribute("user");
        if(user.getId() == article.getUserId()) {
            return RetJson.fail(-1,"非法操作");
        }
        if(articleService.insertArticle(article))
            return RetJson.succcess(null);
        return RetJson.fail(-1,"更新失败！");
    }

    @RequestMapping("/like")
    public RetJson likeArticle(Integer uid,Integer articleId, HttpServletRequest request){
        if(articleService.isLiked(uid,articleId)){
            return RetJson.fail(-1,"重复点赞！");
        }
        articleService.like(uid,articleId);
        return RetJson.succcess(null);
    }

    @RequestMapping("/deleteLike")
    public RetJson deleteLike(Integer uid,Integer articleId, HttpServletRequest request){
        if(articleService.isLiked(uid,articleId)){
            return RetJson.fail(-1,"非法操作！");
        }
        articleService.deleteLikeById(uid,articleId);
        return RetJson.succcess(null);
    }

    @RequestMapping("/getArticle")
    public RetJson getArticleById(Integer articleId, HttpServletRequest request){
        Article article = articleService.getArticle(articleId);
        return RetJson.succcess("Article",article);
    }

}
