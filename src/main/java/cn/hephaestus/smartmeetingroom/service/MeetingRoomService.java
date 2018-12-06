package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.MettingRoom;

public interface MeetingRoomService {

    public boolean addMeetingRoom(MettingRoom mettingRoom);

    public boolean alterMeetingRoom(MettingRoom mettingRoom);

    public boolean delteteMeetingRoom(Integer roomId);

    public MettingRoom getMeetingRoomWithRoomName(String roomName);

    public MettingRoom getMeetingRoomWithRoomId( Integer roomId);

}
