package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.MettingRoom;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MeetingRoomMapper {

    @Insert({"insert into meeting_room(room_name,capacity) values(#{meetingRoom.roomName},#{meetingRoom.capacity})"})
    @Options(useGeneratedKeys = true,keyProperty = "roomId",keyColumn = "room_id")
    public int addMeetingRoom(@Param("meetingRoom") MettingRoom mettingRoom);

    @Update("update meeting_room set room_name=#{meetingRoom.roomName},capacity=#{meetingRoom.capacity}")
    public void alterMeetingRoom(@Param("meetingRoom") MettingRoom mettingRoom);

    @Delete("delete from meeting_room where room_id=#{roomId}")
    public void deleteMeetingRoom(@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_id=#{roomId}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName")
    })
    public MettingRoom getMeetingRoomWithRoomId(@Param("roomId") Integer roomId);

    @Select("select * from meeting_room where room_name=#{roomName}")
    @Results({
            @Result(column = "room_id",property = "roomId"),
            @Result(column = "room_name",property = "roomName")
    })
    public MettingRoom getMeetingRoomWithRoomName(@Param("roomName") String roomName);

}
