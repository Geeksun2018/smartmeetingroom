package cn.hephaestus.smartmeetingroom.service.UserServiceImpl;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.mapper.MeetingRoomMapper;
import cn.hephaestus.smartmeetingroom.model.MettingRoom;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl implements MeetingRoomService {
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public boolean addMeetingRoom(MettingRoom mettingRoom) {
        //需判断当先用户是否有权限
        meetingRoomMapper.addMeetingRoom(mettingRoom);
        return true;
    }

    @Override
    public boolean alterMeetingRoom(MettingRoom mettingRoom) {
        //需判断当先用户是否有权限
        meetingRoomMapper.addMeetingRoom(mettingRoom);
        return true;
    }

    @Override
    public boolean delteteMeetingRoom(Integer roomId) {
        //需判断当先用户是否有权限
        meetingRoomMapper.deleteMeetingRoom(roomId);
        return false;
    }

    @Override
    public MettingRoom getMeetingRoomWithRoomName(String roomName) {
        return meetingRoomMapper.getMeetingRoomWithRoomName(roomName);
    }

    @Override
    public MettingRoom getMeetingRoomWithRoomId(Integer roomId) {
        return meetingRoomMapper.getMeetingRoomWithRoomId(roomId);
    }
}
