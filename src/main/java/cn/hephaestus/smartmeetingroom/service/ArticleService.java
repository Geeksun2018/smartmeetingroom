package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Article;

import java.util.List;

public interface ArticleService {

    public boolean insertArticle(Article article);

    public boolean updateArticle(Article article);

    public boolean deleteArticleByArticleId(Integer articleId);

    public Article getArticle(Integer articleId);

    public List<Integer>  getLikeList(Integer articleId);

    public boolean deleteLikeById(Integer uid,Integer articleId);

    public void like(Integer uid,Integer articleId);

    public boolean isLiked(Integer uid, Integer articleId);
}
