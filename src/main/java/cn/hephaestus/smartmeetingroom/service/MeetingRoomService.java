package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;

public interface MeetingRoomService {

    public boolean addMeetingRoom(MeetingRoom meetingRoom);

    public boolean alterMeetingRoom(MeetingRoom meetingRoom);

    public boolean delteteMeetingRoom(Integer oid,Integer roomId);

    public MeetingRoom getMeetingRoomWithRoomName(String roomName);

    public MeetingRoom getMeetingRoomWithRoomId(Integer roomId);

    public MeetingRoom[] getMeetingRoomList(Integer oid);

    public MeetingRoom getMeetingRoomWithMacAddress(String macAddress);

}
