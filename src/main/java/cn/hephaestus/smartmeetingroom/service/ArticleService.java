package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Article;

import java.util.List;

public interface ArticleService {

    public boolean insertArticle(Article article);

    public Article getArticle(Integer articleId,Integer oid);

    public boolean deleteArticleByArticleId(Integer articleId,Integer uid);


    public List<Integer>  getLikeList(Integer articleId,Integer oid);

    public boolean deleteLikeById(Integer uid,Integer articleId,Integer oid);

    public void like(Integer uid,Integer articleId,Integer oid);

    public boolean isLiked(Integer uid, Integer articleId,Integer oid);

    public List<Article> getNewArticle(Integer articleId,Integer oid);

    public List<Article> getInitArticle(Integer oid);

    List<Article> getLastArticle(Integer articleId,Integer oid);
}
