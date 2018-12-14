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
        return meetingRoomMapper.deleteMeetingRoom(oid,roomId);
    }

    @Override
    public MeetingRoom getMeetingRoomWithRoomName(Integer oid,String roomName) {
        return meetingRoomMapper.getMeetingRoomWithRoomName(oid,roomName);
    }

    @Override
    public MeetingRoom getMeetingRoomWithRoomId(Integer oid,Integer roomId) {
        return meetingRoomMapper.getMeetingRoomWithRoomId(oid,roomId);
    }

    @Override
    public MeetingRoom[] getMeetingRoomList(Integer oid) {
        return meetingRoomMapper.getMeetingRoomList(oid);
    }
}
