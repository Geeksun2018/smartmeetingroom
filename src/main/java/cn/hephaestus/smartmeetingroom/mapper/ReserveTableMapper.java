package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface ReserveTableMapper {

    @Insert({"insert into reserve_table(start_time,end_time,rid,topic,reserve_uid,reserve_oid,reserve_did) values(#{startTime},#{endTime},#{rid},#{topic},#{reserveUid},#{reserveOid},#{reserveDid})"})
    @Options(useGeneratedKeys = true,keyProperty = "reserveId",keyColumn = "reserve_id")
    public boolean addReserveInfo(ReserveInfo reserveInfo);

    @Delete("delete from reserve_table where reserve_id = #{reserveId} and reserve_oid=#{oid}")
    public boolean deleteReserveInfo(@Param("reserveId") Integer reserveId,Integer oid);

    @Update("update reserve_table set start_time=#{startTime},end_time=#{endTime},rid=#{rid},topic=#{topic} where reserve_oid=#{reserveOid} and reserve_uid=#{reserveUid} and reserve_id=#{reserveId}")
    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    @Select("select * from reserve_table where reserve_id = #{reserveId} and reserve_oid=#{oid}")
    @Results(id="reserveInfoMap",value = {
            @Result(property = "reserveId",column = "reserve_id"),
            @Result(property = "startTime",column = "start_time"),
            @Result(property = "endTime",column = "end_time"),
            @Result(property = "reserveUid",column = "reserve_uid"),
            @Result(property = "reserveOid",column = "reserve_oid"),
            @Result(property = "reserveDid",column = "reserve_did"),
    })
    public ReserveInfo getReserveInfoByReserveId(@Param("oid") Integer oid,@Param("reserveId") Integer reserveId);


    @Select("select * from reserve_table where rid = #{rid} and to_days(start_time)=to_days(#{date})")
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] getReserveInfoByRoomId(@Param("rid") Integer rid, @Param("date")Date date);


    @Select("select * from reserve_table where ((#{startTime} > start_time and #{startTime} < end_time)or(#{endTime} > start_time and #{endTime} < end_time) " +
            "or (start_time >= #{startTime} and end_time <= #{endTime})) and rid=#{rid}")
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] queryIsAvailable(@Param("rid") Integer rid,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

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
            "And reserve_oid=#{oid}",
            "</script>"})
    @ResultMap(value = "reserveInfoMap")
    public ReserveInfo[] getReserveInfoByCondition(@Param("date")Date date,@Param("rid")Integer rid,@Param("did")Integer did,@Param("oid")Integer oid);

    @Select("select rid from reserve_table where ((#{beginTime} between start_time and end_time)or " +
            "(#{endTime} between start_time and end_time)) and reserve_id = #{reserveId}")
    public Integer queryIsAvailableByReserveId(@Param("reserveId") Integer reserveId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    @Select("select rid from reserve_table where reserve_oid=#{oid} and ( (#{startTime}>=start_time and #{startTime}<=end_time) or (#{endTime}>=start_time and #{endTime}<=end_time) )")
    public Integer[] queryAllUnUsableMeetingroom(@Param("oid")Integer oid,@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    @Select("select reserve_id from reserve_table where reserve_oid=#{oid} and ( (#{startTime}>=start_time and #{startTime}<=end_time) or (#{endTime}>=start_time and #{endTime}<=end_time) )")
    public Integer[] queryAllUnUsableReserve(@Param("oid")Integer oid,@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    @Select("select reserve_id from reserve_table where reserve_oid=#{oid} and to_days(start_time)=to_days(#{date})")
    public Integer[] queryAllUnUsableReserveByDay(@Param("oid")Integer oid,@Param("date")Date date);

    @Select("select * from reserve_table where reserve_id = #{reserveId} and reserve_oid=#{oid} and reserve_uid=#{uid}")
    @ResultMap(value ="reserveInfoMap")
    public ReserveInfo getReserveInfoByReserveIdanUid(@Param("uid") Integer uid, @Param("oid") Integer oid, @Param("reserveId") Integer reserveId);

    @Select("select count(reserve_id) from reserve_table where reserve_oid=#{oid} and reserve_did=#{did} and year(start_time)=#{year}")
    public Integer queryCountOfDepartmentMeetingByYear(@Param("oid")Integer oid,@Param("did")Integer did,@Param("year")Integer year);

    @Select("select count(reserve_id) from reserve_table where reserve_oid=#{oid} and reserve_did=#{did} and year(start_time)=#{year} and month(start_time)=#{month}")
    public Integer queryCountOfDepartmentMeetingByMonth(@Param("oid")Integer oid,@Param("did")Integer did,@Param("year")Integer year,@Param("month")Integer month);

    @Select("select sum(TIMESTAMPDIFF(MINUTE,start_time,end_time)) from reserve_table where reserve_oid=#{oid} and reserve_did=#{did} and year(start_time)=#{year}")
    public Integer queryCountOfDepartmentMeetingTimeByYear(@Param("oid")Integer oid,@Param("did")Integer did,@Param("year")Integer year);

    @Select("select sum(TIMESTAMPDIFF(MINUTE,start_time,end_time)) from reserve_table where reserve_oid=#{oid} and reserve_did=#{did} and year(start_time)=#{year} and month(start_time)=#{month}")
    public Integer queryCountOfDepartmentMeetingTimeByMonth(@Param("oid")Integer oid,@Param("did")Integer did,@Param("year")Integer year,@Param("month")Integer month);

    @Select("select count(reserve_id) from reserve_table where to_days(start_time)=to_days(#{date})")
    public Integer queryCountOfMeetingByDay(@Param("date")Date date);
}
