package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.CarouselMap;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CarouselMapMapper {
    @Insert("insert into carousel_map(oid,title,link,imagePath,startTime,endTime) values(#{oid},#{title},#{link},#{imagePath},#{startTime},#{endTime})")
    public boolean addCarouselMap(CarouselMap carouselMap);

    @Update("delete from carousel_map where id=#{id} and oid=#{oid}")
    public boolean deleteCarouselMap(@Param("id") int id, @Param("oid") int oid);

    @Update("update carousel_map set title=#{title},startTime=#{startTime},endTime=#{endTime}")
    public boolean alterCarouselMap(CarouselMap carouselMap);

    @Select("select * from carousel_map where oid=#{oid}")
    public CarouselMap[] getAllCarouselMap(int oid);
}
