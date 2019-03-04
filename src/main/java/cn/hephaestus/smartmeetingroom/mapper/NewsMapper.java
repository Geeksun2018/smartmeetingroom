package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.News;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface NewsMapper {

    @Insert("insert into news(sent_id,recive_id,content,time,expire,inform_type) values(#{sentId},#{reciveId},#{content},#{time},#{expire},#{informType})")
    @Options(useGeneratedKeys = true,keyProperty = "newsId",keyColumn = "news_id")
    public boolean insertNews(News news);

    @Select("select * from news where to_days(time) = to_days(#{date})")
    @Results({
            @Result(column = "news_id",property = "newsId"),
            @Result(column = "sent_id",property = "sentId"),
            @Result(column = "recive_id",property = "reciveId"),
            @Result(column = "inform_type",property = "informType")
    })
    public News[] getNewsByDate(Date date);

    @Select("select * from news where inform_type = #{informType}")
    @Results({
            @Result(column = "news_id",property = "newsId"),
            @Result(column = "sent_id",property = "sentId"),
            @Result(column = "recive_id",property = "reciveId"),
            @Result(column = "inform_type",property = "informType")
    })
    public News[] getNewsByType(String informType);

}
