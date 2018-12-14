package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;

public interface MeetingRoomService {

    public boolean addMeetingRoom(MeetingRoom meetingRoom);

    public boolean alterMeetingRoom(MeetingRoom meetingRoom);

    public boolean delteteMeetingRoom(Integer oid,Integer roomId);

    public MeetingRoom getMeetingRoomWithRoomName(Integer oid,String roomName);

    public MeetingRoom getMeetingRoomWithRoomId(Integer oid,Integer roomId);

    public MeetingRoom[] getMeetingRoomList(Integer oid);

}
