package cn.hephaestus.smartmeetingroom.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface MeetingParticipantMapper {
    @Insert({"insert into meeting_participant(mid,uid) values(#{mid},#{uid})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public int addParticipant(Integer mid,Integer uid);

    @Delete({"delete from meeting_participant where mid = #{reserveInfoId} and uid = #{uid}"})
    public boolean deleteParticipant(Integer reserveInfoId,Integer uid);

    @Select({"select uid from meeting_participant where mid = #{reserveInfoId}"})
    public Integer[] getParticipants(Integer reserveInfoId);

    @Delete({"delete from meeting_participant where mid = #{reserveInfoId}"})
    public boolean deleteParticipants(Integer reserveInfoId);
}
