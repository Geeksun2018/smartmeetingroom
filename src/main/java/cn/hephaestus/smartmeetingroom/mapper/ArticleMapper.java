package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Article;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleMapper {

    @Insert({"insert into article(content,user_id,article_name,creat_time,like_list) values(#{content},#{userId},#{articleName},#{creatTime},#{likeList})"})
    @Options(useGeneratedKeys = true,keyProperty = "articleId",keyColumn = "article_id")
    public boolean insertArticle(Article article);

    @Update("update article set content = #{article.content},user_id = #{article.userId}, article_name = #{article.articleName}" +
            " where article_id = #{article.articleId}")
    public boolean updateArticle(@Param("article") Article article);

    @Delete("delete from article where article_id = #{articleId}")
    public boolean deleteArticleByArticleId(@Param("articleId")Integer articleId);

    @Select("select * from article where article_id = #{articleId}")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "article_name",property = "articleName"),
            @Result(column = "creat_time",property = "creatTime"),
            @Result(column = "like_list",property = "likeList"),
            @Result(column = "user_id",property = "userId")
    })
    public Article getArticle(@Param("articleId") Integer articleId);

    @Select("select like_list from article where article_id = #{articleId}")
    public String getLikeList(@Param("articleId") Integer articleId);

    @Update("update article set like_list = #{likeList} where article_id = #{articleId}")
    public boolean updateLikeList(@Param("articleId") Integer articleId,@Param("likeList") String likeList);
}
