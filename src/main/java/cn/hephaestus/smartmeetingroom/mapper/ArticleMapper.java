package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Article;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleMapper {

    @Insert({"insert into article(content,user_id,creat_time) values(#{content},#{userId},#{creatTime})"})
    @Options(useGeneratedKeys = true,keyProperty = "articleId",keyColumn = "article_id")
    public boolean insertArticle(Article article);

    @Update("update article set content = #{content} where article_id = #{articleId} and user_id=#{userId}")
    public boolean updateArticle(Article article);

    @Delete("delete from article where article_id = #{articleId}")
    public boolean deleteArticleByArticleId(@Param("articleId")Integer articleId);

    @Select("select * from article where article_id = #{articleId}")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
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
