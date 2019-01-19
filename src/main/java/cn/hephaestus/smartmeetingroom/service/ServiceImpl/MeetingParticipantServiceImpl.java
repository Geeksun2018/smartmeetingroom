package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingParticipantMapper;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class MeetingParticipantServiceImpl implements MeetingParticipantService {

    @Autowired
    MeetingParticipantMapper meetingParticipantMapper;
    @Autowired
    RedisService redisService;

    @Override
    public int addParticipant(Integer oid,Integer reserveInfoId, Integer uid) {
        if(meetingParticipantMapper.addParticipant(reserveInfoId,uid) == 0){
            return 0;
        }
        Set<String> set = redisService.sget(oid + "cm" + reserveInfoId);
        if(set == null){
            Integer[] participants = meetingParticipantMapper.getParticipants(reserveInfoId);
            redisService.sadd(oid + "cm" + reserveInfoId,toStringArray(participants));
            redisService.expire(oid  + "cm" + reserveInfoId,31536000);
        }else{
            redisService.sadd(oid + "cm" + reserveInfoId,uid.toString());
        }
        return 1;
    }

    @Override
    public boolean deleteParticipant(Integer oid,Integer reserveInfoId, Integer uid) {
        if(!meetingParticipantMapper.deleteParticipant(reserveInfoId,uid)){
            return false;
        }
        Set<String> set = redisService.sget(oid + "cm" + reserveInfoId);
        if(set == null){
            Integer[] participants = meetingParticipantMapper.getParticipants(reserveInfoId);
            redisService.sadd(oid + "cm" + reserveInfoId,toStringArray(participants));
            redisService.expire(oid  + "cm" + reserveInfoId,31536000);
        }else{
            redisService.sdel(oid + "cm" + reserveInfoId,uid.toString());
        }
        return true;
    }

    @Override
    public Integer[] getParticipants(Integer oid,Integer reserveInfoId) {
        Set<String> set = redisService.sget(oid + "cm" + reserveInfoId);
        Integer[] participants = null;
        //如果没在redis中查到
        if(set == null) {
            participants = meetingParticipantMapper.getParticipants(reserveInfoId);
            if (participants != null) {
                redisService.sadd(oid + "cm" + reserveInfoId, toStringArray(participants));
                redisService.expire(oid  + "cm" + reserveInfoId,31536000);
            }
        }else{
            participants = toIntegerArray(set);
        }
        return participants;
    }

    @Override
    public boolean addParticipants(Integer oid,Integer mid, Integer[] uids) {
        for(Integer uid:uids){
            meetingParticipantMapper.addParticipant(mid,uid);
        }
        redisService.sadd(oid + "cm" + mid,toStringArray(uids));
        redisService.expire(oid  + "cm" + mid,31536000);
        return true;
    }

    @Override
    public boolean deleteParticipants(Integer oid,Integer reserveInfoId) {
        redisService.del(oid + "cm" + reserveInfoId);
        return meetingParticipantMapper.deleteParticipants(reserveInfoId);
    }

    Integer[] toIntegerArray(Set set){
        List<Integer> integers = new ArrayList<>();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String str = it.next();
            integers.add(Integer.parseInt(str));
        }
        return integers.toArray(new Integer[0]);
    }

    String[] toStringArray(Integer[] arrays){
        List<String> lString = new ArrayList<>();
        for(Integer num:arrays){
            lString.add(num.toString());
        }
        return lString.toArray(new String[0]);
    }
}
