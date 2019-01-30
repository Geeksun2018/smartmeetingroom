package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface ReserveTableMapper {

    @Insert({"insert into reserve_table(start_time,end_time,rid,topic,reserve_uid,reserve_oid,reserve_did) values(#{startTime},#{endTime},#{rid},#{meetingTopic},#{uid},#{oid},#{did})"})
    @Options(useGeneratedKeys = true,keyProperty = "reserveId",keyColumn = "reserve_id")
    public boolean addReserveInfo(ReserveInfo reserveInfo);

    @Delete("delete from reserve_table where reserve_id = #{reserveId} and reserve_uid=#{uid}")
    public boolean deleteReserveInfo(@Param("reserveId") Integer reserveId,@Param("uid")Integer uid);

    @Update("update reserve_table set start_time=#{startTime},end_time=#{endTime},rid=#{rid},topic=#{topic} where reserve_uid=#{uid}")
    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    @Select("select * from reserve_table where reserve_id = #{reserveId} and reserve_oid=#{oid}")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time"),
            @Result(property ="uid",column ="reserve_uid" ),
            @Result(property = "oid",column = "reserve_oid")
    })
    public ReserveInfo getReserveInfoByReserveId(@Param("oid")Integer oid,@Param("reserveId") Integer reserveId);

    @Select("select * from reserve_table where rid = #{rid} and to_days(start_time)=to_days(#{date})")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time")
    })
    public ReserveInfo[] getReserveInfoByRoomId(@Param("rid") Integer rid, @Param("date")Date date);

    @Select("select * from reserve_table where ((#{startTime} > start_time and #{startTime} < end_time)or(#{endTime} > start_time and #{endTime} < end_time) " +
            "or (start_time >= #{startTime} and end_time <= #{endTime})) and rid=#{rid}")
    @Results({
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time")
    })
    public ReserveInfo[] queryIsAvailable(@Param("rid") Integer rid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select rid from reserve_table where ((#{beginTime} between start_time and end_time)or " +
            "(#{endTime} between start_time and end_time)) and reserve_id = #{reserveId}")
    public Integer queryIsAvailableByReserveId(@Param("reserveId") Integer reserveId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);

    @Select("select rid from reserve_table where reserve_oid=#{oid} and ( (#{startTime}>=start_time and #{startTime}<=end_time) or (#{endTime}>=start_time and #{endTime}<=end_time) )")
    public Integer[] queryAllUnUsableMeetingroom(@Param("oid")Integer oid,@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    @Select("select reserve_id from reserve_table where reserve_oid=#{oid} and ( (#{startTime}>=start_time and #{startTime}<=end_time) or (#{endTime}>=start_time and #{endTime}<=end_time) )")
    public Integer[] queryAllUnUsableReserve(@Param("oid")Integer oid,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
}
