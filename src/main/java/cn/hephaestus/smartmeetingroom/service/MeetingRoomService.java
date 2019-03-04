package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;

import java.util.Date;
import java.util.Set;

public interface MeetingRoomService {

    public boolean addMeetingRoom(MeetingRoom meetingRoom);

    public boolean alterMeetingRoom(MeetingRoom meetingRoom);

    public boolean delteteMeetingRoom(Integer oid,Integer roomId);

    public MeetingRoom getMeetingRoomWithRoomName(String roomName);

    public boolean judgeMeeting(String md5);

    public MeetingRoom getMeetingRoomWithRoomId(Integer roomId);

    public MeetingRoom[] getMeetingRoomList(Integer oid);

    Set<Integer> getAllConficUser(Integer oid,Date startTime, Date endTime);

    public MeetingRoom[] getAllUsableMeetingRooms(Integer oid, Date startTime, Date endTime, Integer num);

    public MeetingRoom getMeetingRoomWithMacAddress(String macAddress);

    public ReserveInfo getProperMeetingTime(Set<Integer> participants, Date[] dates, double duration,Integer oid);

}
