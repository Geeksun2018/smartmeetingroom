package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingRoomMapper;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl implements MeetingRoomService {
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public boolean addMeetingRoom(MeetingRoom meetingRoom) {
        //需判断当先用户是否有权限
        meetingRoomMapper.addMeetingRoom(meetingRoom);
        return true;
    }

    @Override
    public boolean alterMeetingRoom(MeetingRoom meetingRoom) {
        //需判断当先用户是否有权限
        meetingRoomMapper.alterMeetingRoom(meetingRoom);
        return true;
    }

    @Override
    public boolean delteteMeetingRoom(Integer oid,Integer roomId) {
        //需判断当先用户是否有权限
        meetingRoomMapper.deleteMeetingRoom(oid,roomId);
        return true;
    }

    @Override
    public MeetingRoom getMeetingRoomWithRoomName(String roomName) {
        return meetingRoomMapper.getMeetingRoomWithRoomName(roomName);
    }

    @Override
    public MeetingRoom getMeetingRoomWithRoomId(Integer roomId) {
        return meetingRoomMapper.getMeetingRoomWithRoomId(roomId);
    }

    @Override
    public MeetingRoom[] getMeetingRoomList(Integer oid) {
        return meetingRoomMapper.getMeetingRoomList(oid);
    }

    @Override
    public MeetingRoom getMeetingRoomWithMacAddress(String macAddress) {
        return meetingRoomMapper.getMeetingRoomWithMacAddress(macAddress);
    }
}
