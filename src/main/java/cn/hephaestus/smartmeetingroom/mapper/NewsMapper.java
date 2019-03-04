package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface NewsMapper {

    @Insert("insert into news(sent_id,recive_id,content,time,expire,inform_type) values(#{sentId},#{reciveId},#{content},#{time},#{expire},#{informType})")
    @Options(useGeneratedKeys = true,keyProperty = "newsId",keyColumn = "news_id")
    public boolean insertNews(News news);

    //根据前端要求具体再修改，暂时不做实现
    public News[] getNews();

}
