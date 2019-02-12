package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingParticipantMapper;
import cn.hephaestus.smartmeetingroom.mapper.MeetingRoomMapper;
import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.ReserveInfoService;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {

    @Autowired
    private ReserveTableMapper reserveTableMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    @Autowired
    private MeetingParticipantMapper meetingParticipantMapper;


    @Override
    public boolean addReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.addReserveInfo(reserveInfo);
    }

    @Override
    public boolean deleteReserveInfo(Integer oid, Integer reserveId) {
        //先删除所有预定者,有bug,表的设计不合理（暂时不解决）
       meetingParticipantMapper.deleteParticipants(reserveId);
       if(reserveTableMapper.deleteReserveInfo(reserveId,oid)){
            redisService.del(oid + "cm" + reserveId);
            return true;
       }
        return false;
    }

    @Override
    public boolean updateReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.updateReserveInfo(reserveInfo);
    }

    @Override
    public ReserveInfo getReserveInfoByReserveId(Integer oid,Integer reserveId) {
        return reserveTableMapper.getReserveInfoByReserveId(oid,reserveId);
    }

    @Override
    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date) {
        return reserveTableMapper.getReserveInfoByRoomId(rid,date);
    }

    @Override
    public ReserveInfo[] queryIsAvailable(Integer rid, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvailable(rid,beginTime,endTime);
    }

    @Override
    public Integer queryIsAvailableByReserveId(Integer reserveId, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvailableByReserveId(reserveId,beginTime,endTime);
    }

    @Override
    public List<ReserveInfoViewObject> getReserveInfoViewObjectByCondition(Date date, Integer rid, Integer did,Integer oid) {
        ReserveInfo[] reserveInfos = reserveTableMapper.getReserveInfoByCondition(date,rid,did,oid);
        List<ReserveInfoViewObject> list = new ArrayList<>();
        for(ReserveInfo reserveInfo:reserveInfos){
            String roomName = meetingRoomMapper.getMeetingRoomWithRoomId(reserveInfo.getReserveId()).getRoomName();
            Integer[] participants = meetingParticipantMapper.getParticipants(reserveInfo.getReserveId());
            list.add(new ReserveInfoViewObject(roomName,reserveInfo.getTopic(),reserveInfo.getStartTime(),
                    reserveInfo.getEndTime(),participants,reserveInfo.getFlag(),reserveInfo.getReserveUid(),
                    reserveInfo.getReserveDid()));
        }
        return list;
    }
}
