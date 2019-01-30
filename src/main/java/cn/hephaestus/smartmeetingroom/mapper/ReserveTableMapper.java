package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface ReserveTableMapper {

    @Insert({"insert into reserve_table(start_time,end_time,rid,topic) values(#{startTime},#{endTime},#{rid},#{topic})"})
    @Options(useGeneratedKeys = true,keyProperty = "reserveId",keyColumn = "reserve_id")
    public boolean addReserveInfo(ReserveInfo reserveInfo);

    @Delete("delete from reserve_table where reserve_id = #{reserveId}")
    public boolean deleteReserveInfo(@Param("reserveId") Integer reserveId);

    @Update("update reserve_table set start_time=#{startTime},end_time=#{endTime},rid=#{rid},topic=#{topic}")
    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    @Select("select * from reserve_table where reserve_id = #{reserveId}")
    @Results(id="reserveInfoMap",value = {
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time"),
            @Result(property = "reserveUid",column = "reserve_uid"),
            @Result(property = "reserveOid",column = "reserve_oid"),
            @Result(property = "reserveDid",column = "reserve_did"),
    })
    public ReserveInfo getReserveInfoByReserveId(@Param("reserveId") Integer reserveId);

    @Select("select * from reserve_table where rid = #{rid} and to_days(start_time)=to_days(#{date})")
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] getReserveInfoByRoomId(@Param("rid") Integer rid, @Param("date")Date date);

    @Select("select * from reserve_table where ((#{startTime} > start_time and #{startTime} < end_time)or(#{endTime} > start_time and #{endTime} < end_time) " +
            "or (start_time >= #{startTime} and end_time <= #{endTime})) and rid=#{rid}")
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] queryIsAvailable(@Param("rid") Integer rid,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select({"<script>",
            "SELECT * FROM reserve_table",
            "WHERE 1=1",
            "<when test='date!=null'>",
            "AND to_days(start_time) = to_days(#{date})",
            "</when>",
            "<when test='rid!=null'>",
            "AND rid = #{rid}",
            "</when>",
            "<when test='did!=null'>",
            "AND reserve_did = #{did}",
            "</when>",
            "</script>"})
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] getReserveInfoByCondition(@Param("date")Date date,@Param("rid")Integer rid,@Param("did")Integer did);

    @Select("select rid from reserve_table where ((#{beginTime} between start_time and end_time)or " +
            "(#{endTime} between start_time and end_time)) and reserve_id = #{reserveId}")
    public Integer queryIsAvailableByReserveId(@Param("reserveId") Integer reserveId,@Param("beginTime") String beginTime,@Param("endTime") String endTime);


}
