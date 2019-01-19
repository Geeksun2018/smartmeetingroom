package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Comment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CommentMapper {

    @Insert({"insert into article_comment(article_id,content,uid) values(#{articleId},#{content},#{uid})"})
    @Options(useGeneratedKeys = true,keyProperty = "commentId",keyColumn = "comment_id")
    public boolean insertComment(Comment comment);

    @Delete("delete from article_comment where comment_id = #{commentId}")
    public boolean deleteComment(@Param("commentId") Integer commentId);

    @Select("select * from article_comment where article_id = #{articleId}")
    @Results({
            @Result(column = "comment_id",property = "commentId"),
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
    })
    public Comment[] getComments(@Param("articleId") Integer articleId);

    @Update("update article_comment set article_id = #{comment.articleId},content = #{comment.content},uid = #{comment.uid} where " +
            "comment_id = #{comment.commentId}")
    public boolean updateComment(@Param("comment") Comment comment);

    @Select("select * from article_comment where comment_id = #{commentId}")
    @Results({
            @Result(column = "comment_id",property = "commentId"),
            @Result(column = "article_id",property = "articleId"),
            @Result(column = "creat_time",property = "creatTime"),
    })
    public Comment getComment(@Param("commentId") Integer commentId);

    @Delete("delete * from article_comment where article_id = #{articleId}")
    public boolean deleteCommentsByArticleId(@Param("articleId") Integer articleId);

}
