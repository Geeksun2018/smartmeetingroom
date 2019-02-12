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

import java.util.*;

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
    public boolean deleteReserveInfo(Integer oid,Integer reserveId) {
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

    @Override
    public Integer queryCountOfDepartmentMeetingByYear(Integer oid, Integer did, Integer year) {
        return reserveTableMapper.queryCountOfDepartmentMeetingByYear(oid,did,year);
    }

    @Override
    public Integer queryCountOfDepartmentMeetingByMonth(Integer oid, Integer did, Integer year, Integer month) {
        return reserveTableMapper.queryCountOfDepartmentMeetingByMonth(oid,did,year,month);
    }

    @Override
    public Integer queryCountOfDepartmentMeetingTimeByYear(Integer oid, Integer did, Integer year) {
        return reserveTableMapper.queryCountOfDepartmentMeetingTimeByYear(oid,did,year);
    }

    @Override
    public Integer queryCountOfDepartmentMeetingTimeByMonth(Integer oid, Integer did, Integer year, Integer month) {
        return reserveTableMapper.queryCountOfDepartmentMeetingTimeByMonth(oid,did,year,month);
    }

    @Override
    public Map<Date,Integer> queryCountOfMeetingByDay(Date date) {
        Map<Date,Integer> map = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        for (int i = 0; i < 7; i++) {
            date = calendar.getTime();
            Integer count = reserveTableMapper.queryCountOfMeetingByDay(date);
            if(count == null){
                map.put(date,0);
            }else{
                map.put(date,count);
            }
            calendar.add(Calendar.DATE, 1);
        }
        return map;
    }
}
