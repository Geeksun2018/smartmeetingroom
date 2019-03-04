package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingRoomMapper;
import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
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

    private Calendar calendar = Calendar.getInstance();

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
    public boolean judgeMeeting(String md5) {
        MeetingRoom[] meetingRooms=meetingRoomMapper.judgeMeeting(md5);
        if (meetingRooms.length==0){
            return false;
        }else {
            return true;
        }
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

    @Override
    public ReserveInfo getProperMeetingTime(Set<Integer> participants, Date[] dates, double duration,Integer oid) {
        Integer[] temp=null;
        for(Date date:dates){
            List<Integer> list = new ArrayList<>();
            Integer[] rids = null;
            MeetingRoom[] rooms = meetingRoomMapper.getMeetingRoomList(oid);
            //每个房间都判断一遍
            for(int k = 0;k < rooms.length;k++){
                if(rooms[k].getCapacity() <participants.size()){
                    continue;
                }
                rids = reserveTableMapper.queryAllUnUsableReserveByDay(oid,date);
                //把所有成员参加的会议和这个会议室的会议都加进集合
                ReserveInfo tempInfo = null;
                for (Integer rid:rids){
                    temp=meetingParticipantService.getParticipants(oid,rid);
                    tempInfo = reserveTableMapper.getReserveInfoByReserveId(oid,rid);
                    //取出所有在该房间开会的会议
                    if(tempInfo.getRid() == rooms[k].getRoomId()){
                        list.add(rid);
                        continue;
                    }
                    //取出所有成员的会议
                    for (int i=0;i<temp.length;i++){
                        if(participants.contains(temp[i])){
                            list.add(rid);
                            break;
                        }
                    }
                }
                //工作时间为 早上8点到中午12点 下午两点到10点
                //每个点是否为1 代表前半个小时是否被占用
                Integer[] time = new Integer[29];
                for(int i = 0;i < time.length;i++){
                    time[i] = 0;
                }
                ReserveInfo reserveInfo = null;
                Date startTime = null;
                Date endTime = null;
                for(int i = 0;i < list.size();i++){
                    reserveInfo = reserveTableMapper.getReserveInfoByReserveId(oid,list.get(i));
                    startTime = reserveInfo.getStartTime();
                    endTime = reserveInfo.getEndTime();
                    signTime(time,startTime,endTime);
                }
                int end = 0;
                //从第一个点开始判断，直到最后一个点
                //1代表八点到八点半，24代表九点半到十点
                for(int i = 1;i <= 24;i++) {
                    int flag = 0;
                    end = (int)(i + duration * 60 / 30);
                    if (end > 24) {
                        break;
                    }
                    for (int j = i; j < end; j++) {
                        if (time[j] == 1) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        startTime = getDate(date, i);
                        endTime = getDate(date, end);
                        ReserveInfo tempReserveInfo = new ReserveInfo();
                        tempReserveInfo.setStartTime(startTime);
                        tempReserveInfo.setEndTime(endTime);
                        tempReserveInfo.setRid(rooms[k].getRoomId());
                        return tempReserveInfo;
                    }
                }
            }
        }
        return null;
    }

    private void signTime(Integer[] time,Date startTime,Date endTime){

        int start = getTimeIndex(startTime);
        int end = getTimeIndex(endTime);
        for(int i = start + 1;i <= end;i++){
            time[i] = 1;
        }
    }

    private int getTimeIndex(Date date){
        calendar.setTime(date);
        int index = 0;
        if(calendar.get(Calendar.HOUR_OF_DAY) <= 12){
            index += (((calendar.get(Calendar.HOUR_OF_DAY) - 8) * 2));
        }else{
            index += (((calendar.get(Calendar.HOUR_OF_DAY) - 10) * 2));
        }
        if(calendar.get(Calendar.MINUTE) == 30){
            index += 1;
        }
        return index;
    }

    public Date getDate(Date date,Integer index){
        calendar.setTime(date);
        index -= 1;
        Integer hour = index * 30 / 60;
        Integer minute = index * 30 % 60;
        if(index <= 8){
            calendar.set(Calendar.HOUR_OF_DAY,hour + 8);
            calendar.set(Calendar.MINUTE,minute);
        }else{
            calendar.set(Calendar.HOUR_OF_DAY,hour + 10);
            calendar.set(Calendar.MINUTE,minute);
        }

        return calendar.getTime();
    }
}