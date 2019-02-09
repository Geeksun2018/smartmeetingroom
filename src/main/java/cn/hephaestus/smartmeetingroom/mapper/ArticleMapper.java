package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Article;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert({"insert into article(content,user_id,creat_time,oid) values(#{content},#{userId},#{creatTime},#{oid})"})
    @Options(useGeneratedKeys = true,keyProperty = "articleId",keyColumn = "article_id")
    public boolean insertArticle(Article article);

    @Update("update article set content = #{content} where article_id = #{articleId} and user_id=#{userId}")
    public boolean updateArticle(Article article);

    @Delete("delete from article where article_id = #{articleId} and user_id=#{uid}")
    public boolean deleteArticleByArticleId(@Param("articleId")Integer articleId, @Param("uid")Integer uid);

    @Select("select * from article where article_id = #{articleId} and oid = #{oid}")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
            @Result(column = "like_list",property = "likeList"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "read_count",property = "readCount"),
            @Result(column = "comment_count",property = "commentCount")
    })
    public Article getArticleById(@Param("articleId") Integer articleId,@Param("oid") Integer oid);

    @Select("select * from article where article_id > #{articleId} and oid=#{oid} order by article_id desc")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
            @Result(column = "like_list",property = "likeList"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "read_count",property = "readCount"),
            @Result(column = "comment_count",property = "commentCount")
    })
    public List<Article> getNewArticle(@Param("articleId") Integer articleId, @Param("oid")Integer oid);

    @Select("select * from article where oid=#{oid} order by article_id desc limit 10")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
            @Result(column = "like_list",property = "likeList"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "read_count",property = "readCount"),
            @Result(column = "comment_count",property = "commentCount")
    })
    public List<Article> getInitArticle(@Param("oid")Integer oid);

    @Select("select * from article where article_id < #{articleId} and oid = #{oid} order by article_id desc limit 10")
    @Results(value = {
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
            @Result(column = "like_list",property = "likeList"),
            @Result(column = "user_id",property = "userId"),
            @Result(column = "read_count",property = "readCount"),
            @Result(column = "comment_count",property = "commentCount")
    })
    public List<Article> getLastArticle(@Param("articleId")Integer articleId,@Param("oid")Integer oid);

    @Select("select like_list from article where article_id = #{articleId} and oid=#{oid}")
    public String getLikeList(@Param("articleId") Integer articleId,@Param("oid")Integer oid);

    @Update("update article set like_list = #{likeList} where article_id = #{articleId}")
    public boolean updateLikeList(@Param("articleId") Integer articleId,@Param("likeList") String likeList);


}
