package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.ArticleMapper;
import cn.hephaestus.smartmeetingroom.model.Article;
import cn.hephaestus.smartmeetingroom.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public boolean insertArticle(Article article) {
        return articleMapper.insertArticle(article);
    }

    @Override
    public boolean updateArticle(Article article) {
        return articleMapper.updateArticle(article);
    }

    @Override
    public boolean deleteArticleByArticleId(Integer articleId) {
        return articleMapper.deleteArticleByArticleId(articleId);
    }

    @Override
    public Article getArticle(Integer articleId) {
        return articleMapper.getArticle(articleId);
    }

    @Override
    public List<Article> getDepartmentArticle(Integer uid) {

        return null;
    }

    @Override
    public List<Integer> getLikeList(Integer articleId) {
        List<Integer> userIdList = new LinkedList<>();
        String[] userIdStrings = articleMapper.getLikeList(articleId).split("\\.");
        if (userIdStrings[0].equals("")||userIdStrings==null||userIdStrings.length==0){
            return null;
        }
        for(int i = 0;i < userIdStrings.length;i++){
            ((LinkedList<Integer>) userIdList).push(Integer.parseInt(userIdStrings[i]));
        }
        return userIdList;
    }

    @Override
    public boolean deleteLikeById(Integer uid,Integer articleId) {
        List<Integer> userIdList = new ArrayList<>();
        String[] userIdStrings = articleMapper.getLikeList(articleId).split("\\.");
        for(int i = 0;i < userIdStrings.length;i++){
            Integer userId = Integer.parseInt(userIdStrings[i]);
            if(userId != uid){
                userIdList.add(userId);
            }
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0;i < userIdList.size(); i++) {
            b.append(userIdList.get(i));
            if(i != userIdList.size() - 1)
                b.append(".");
        }
        return articleMapper.updateLikeList(articleId,b.toString());
    }

    @Override
    public void like(Integer uid, Integer articleId) {
        String likeList = articleMapper.getLikeList(articleId);
        if(likeList==null||likeList.endsWith("")){
            likeList = uid.toString();
            articleMapper.updateLikeList(articleId,likeList);
            return;
        }
        StringBuilder b = new StringBuilder(likeList);
        b.append('.');
        b.append(uid);
        articleMapper.updateLikeList(articleId,b.toString());
    }

    public boolean isLiked(Integer uid, Integer articleId){
        List<Integer> list=getLikeList(articleId);
        if (list==null){
            return false;
        }
        return getLikeList(articleId).contains(uid);
    }
}
