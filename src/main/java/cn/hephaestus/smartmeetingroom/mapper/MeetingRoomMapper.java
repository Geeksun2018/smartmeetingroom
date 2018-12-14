package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MeetingRoomMapper {

    @Insert({"insert into meeting_room(room_name,capacity,address,oid) values(#{roomName},#{capacity},#{address},#{oid})"})
    @Options(useGeneratedKeys = true,keyProperty = "roomId",keyColumn = "room_id")
    public int addMeetingRoom(MeetingRoom meetingRoom);

    @Update("update meeting_room set room_name=#{roomName},capacity=#{capacity},address=#{address} where oid=#{oid}")
    public void alterMeetingRoom(MeetingRoom meetingRoom);

    @Delete("delete from meeting_room where room_id=#{roomId} and oid=#{oid}")
    public Boolean deleteMeetingRoom(@Param("oid") Integer oid,@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_id=#{roomId} and oid=#{oid}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName")
    })
    public MeetingRoom getMeetingRoomWithRoomId(@Param("oid") Integer oid,@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_name=#{roomName} and oid=#{oid}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName")
    })
    public MeetingRoom getMeetingRoomWithRoomName(@Param("oid") Integer oid,@Param("roomName") String roomName);

    @Select("select * from meeting_room where oid=#{oid}")
    public MeetingRoom[] getMeetingRoomList(@Param("oid") Integer oid);
}
