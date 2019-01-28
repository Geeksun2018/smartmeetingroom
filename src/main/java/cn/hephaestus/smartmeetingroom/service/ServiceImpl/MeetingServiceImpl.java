package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingRoomMapper;
import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MeetingServiceImpl implements MeetingRoomService {
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Autowired
    private ReserveTableMapper reserveTableMapper;

    @Autowired
    private MeetingParticipantService meetingParticipantService;

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
    public Set<Integer> getAllConficUser(Integer oid,Date startTime,Date endTime){
        //获取当前时段的所有会议
        Integer[] rids=reserveTableMapper.queryAllUnUsableReserve(oid,startTime,endTime);

        Set<Integer> set=new HashSet<>();

        Integer[] temp=null;

        //获取当前会议所有的用户
        for (Integer rid:rids){
            temp=meetingParticipantService.getParticipants(oid,rid);
            for (int i=0;i<temp.length;i++){
                set.add(temp[i]);
            }
        }
        return set;
    }


    @Override
    public MeetingRoom[] getAllUsableMeetingRooms(Integer oid,Date startTime,Date endTime,Integer num) {
        List<MeetingRoom> list=new ArrayList<>();
        MeetingRoom[] meetingRooms=meetingRoomMapper.getMeetingRoomList(oid);//获取所有的会议室
        Integer[] integers=reserveTableMapper.queryAllUnUsableMeetingroom(oid,startTime,endTime);//该公司当前时段不可用的会议室id
        Set<Integer> set=new HashSet<>();
        for (Integer integer:integers){
            set.add(integer);
        }
        for (MeetingRoom meetingRoom:meetingRooms){
            if ( (!set.contains(meetingRoom.getRoomId()) )&&meetingRoom.getCapacity()>=num&&meetingRoom.isAvailable()){
                meetingRoom.setMacAddress(null);
                list.add(meetingRoom);
            }
        }
        MeetingRoom[] arr=new MeetingRoom[list.size()];
        list.toArray(arr);
        return arr;
    }

    @Override
    public MeetingRoom getMeetingRoomWithMacAddress(String macAddress) {
        return meetingRoomMapper.getMeetingRoomWithMacAddress(macAddress);
    }
}
