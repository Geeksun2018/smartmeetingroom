package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MeetingRoomMapper {

    @Insert({"insert into meeting_room(room_name,capacity,address,oid,available,image) values(#{roomName},#{capacity},#{address},#{oid},#{available},#{image})"})
    @Options(useGeneratedKeys = true,keyProperty = "roomId",keyColumn = "room_id")
    public int addMeetingRoom(MeetingRoom meetingRoom);

    @Update("update meeting_room set room_name=#{roomName},capacity=#{capacity},address=#{address},available=#{available},image=#{image} where room_id=#{roomId}")
    public void alterMeetingRoom(MeetingRoom meetingRoom);

    @Delete("delete from meeting_room where room_id=#{roomId} and oid=#{oid}")
    public void deleteMeetingRoom(@Param("oid") Integer oid,@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_id=#{roomId}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName"),
            @Result(column = "mac_address",property ="macAddress"),
            @Result(column = "register_time",property = "registerTime")
    })
    public MeetingRoom getMeetingRoomWithRoomId(@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_name=#{roomName} and oid=#{oid}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName"),
            @Result(column = "mac_address",property ="macAddress"),
            @Result(column = "register_time",property = "registerTime")
    })
    public MeetingRoom getMeetingRoomWithRoomName(@Param("roomName") String roomName);

    @Select("select * from meeting_room where mac_address=#{macAddress}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName"),
            @Result(column = "mac_address",property ="macAddress"),
            @Result(column = "register_time",property = "registerTime")
    })
    public MeetingRoom getMeetingRoomWithMacAddress(String macAddress);

    @Select("select * from meeting_room where oid=#{oid}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName"),
            @Result(column = "mac_address",property ="macAddress"),
            @Result(column = "register_time",property = "registerTime")
    })
    public MeetingRoom[] getMeetingRoomList(@Param("oid") Integer oid);

    @Select("select * from meeting_room where mac_address=#{md5}")
    public MeetingRoom[] judgeMeeting(String md5);

}
