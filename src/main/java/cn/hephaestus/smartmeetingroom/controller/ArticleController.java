package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import cn.hephaestus.smartmeetingroom.vo.ArticleInfoViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ArticleController {

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
    //上传文章
    @RequestMapping("/uploadArticle")
    public RetJson uploadArticle(Article article){
        //校验参数！
        article.setUserId(user.getId());
        article.setOid(userInfo.getOid());
        article.setCreatTime(new Date());

        if(articleService.insertArticle(article)){
            return RetJson.succcess("articleId",article.getArticleId());
        }
        return RetJson.fail(-1,"插入失败！");
    }
    //删除文章
    @RequestMapping("/deleteArticle")
    public RetJson deleteArticle(Integer articleId){
        Integer uid=user.getId();
        if (articleService.deleteArticleByArticleId(articleId,uid)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"删除失败!");
    }

    //点赞
    @RequestMapping("/like")
    public RetJson likeArticle(Integer articleId){
        Integer uid=user.getId();
        Integer oid=userInfo.getOid();
        if(articleService.isLiked(uid,articleId,oid)){
            return RetJson.fail(-1,"重复点赞！");
        }
        articleService.like(uid,articleId,oid);
        return RetJson.succcess(null);
    }

    //取消赞
    @RequestMapping("/deleteLike")
    public RetJson deleteLike(Integer articleId){
        Integer uid=user.getId();
        Integer oid=userInfo.getOid();
        if(!articleService.isLiked(uid,articleId,oid)){
            return RetJson.fail(-1,"非法操作！");
        }
        articleService.deleteLikeById(uid,articleId,oid);
        return RetJson.succcess(null);
    }

    //获取动态，更新，下拉查看
    @RequestMapping("/getArticle")
    public RetJson getArticleById(Integer articleId,String type){
        Integer oid=userInfo.getOid();
        List<Article> list=null;
        if (type.equals("new")){
           //获取刷新的动态
           list=articleService.getNewArticle(articleId,oid);
        }else if (type.equals("init")){
           list=articleService.getInitArticle(oid);
        }else if (type.equals("last")){
           list=articleService.getLastArticle(articleId,oid);
        }
        //组织视图
        List<ArticleInfoViewObject> vo=new LinkedList<>();
        for (Article article:list){
            ArticleInfoViewObject temp=new ArticleInfoViewObject();
            temp.setContent(article.getContent());
            temp.setCreateTime(article.getCreatTime());
            temp.setUid(article.getUserId());
            temp.setId(article.getArticleId());
            temp.setReadCount(article.getReadCount());
            temp.setCommentCount(article.getCommentCount());

            String s=article.getLikeList();
            if (s!=null){
                String[] arr=s.split("\\.");
                temp.setLike(false);
                for (int i=0;i<arr.length;i++){
                    if (arr[i].equals(user.getId().toString())){
                        temp.setLike(true);
                    }
                }
                temp.setLikeCound(arr.length);
            }else {
                temp.setLikeCound(0);
                temp.setLike(false);
            }
            vo.add(temp);
        }
        return RetJson.succcess("ArticleList",vo);
    }
}
