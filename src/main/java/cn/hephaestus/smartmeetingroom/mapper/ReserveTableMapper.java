package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ReserveTableMapper {

    @Insert({"insert into reserve_table(start_time,end_time,rid) values(#{startTime},#{endTime},#{rid})"})
    @Options(useGeneratedKeys = true,keyProperty = "reserveId",keyColumn = "reserve_id")
    public int addReserveInfo(ReserveInfo reserveInfo);

    @Delete("delete from reserve_table where reserve_id = #{reserveId}")
    public boolean deleteReserveInfo(@Param("reserveId") Integer reserveId);

    @Update("update reserve_table set start_time=#{startTime},end_time=#{endTime},rid=#{rid}")
    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    @Select("select * from reserve_table where reserve_id = #{reserveId}")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time")
    })
    public ReserveInfo getReserveInfoByReserveId(@Param("reserveId") Integer reserveId);

    @Select("select * from reserve_table where rid = #{rid}")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time")
    })
    public ReserveInfo[] getReserveInfoByRoomId(@Param("rid") Integer rid);

    @Select("select * from reserve_table where ((#{beginTime} between start_time and end_time)or " +
            "(#{endTime} between start_time and end_time)) and rid = rid")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time")
    })
    public ReserveInfo[] queryIsAvaliable(@Param("rid") Integer rid,@Param("beginTime") String beginTime,@Param("endTime") String endTime);
}
