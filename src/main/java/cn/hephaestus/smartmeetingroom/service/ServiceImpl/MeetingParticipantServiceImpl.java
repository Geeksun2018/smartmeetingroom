package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.MeetingParticipantMapper;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingParticipantServiceImpl implements MeetingParticipantService {

    @Autowired
    MeetingParticipantMapper meetingParticipantMapper;

    @Override
    public int addParticant(Integer reserveInfoId, Integer uid) {
        return meetingParticipantMapper.addParticipant(reserveInfoId,uid);
    }

    @Override
    public boolean deleteParticant(Integer reserveInfoId, Integer uid) {
        return meetingParticipantMapper.deleteParticipant(reserveInfoId,uid);
    }

    @Override
    public Integer[] getParticipants(Integer reserveInfoId) {
        return meetingParticipantMapper.getParticipants(reserveInfoId);
    }

    @Override
    public boolean addParticipants(Integer mid, Integer[] uids) {
        for(Integer uid:uids){
            meetingParticipantMapper.addParticipant(mid,uid);
        }
        return true;
    }

    @Override
    public boolean deleteParticipants(Integer reserveInfoId) {
        return meetingParticipantMapper.deleteParticipants(reserveInfoId);
    }
}
