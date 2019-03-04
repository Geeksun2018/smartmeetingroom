package cn.hephaestus.smartmeetingroom.service;

import java.util.Date;

public interface MeetingParticipantService {
    public int addParticipant(Integer oid,Integer reserveInfoId,Integer uid);

    public boolean deleteParticipant(Integer oid,Integer reserveInfoId,Integer uid);

    public Integer[] getParticipants(Integer oid,Integer reserveInfoId);

    public Integer[] getParticipantsByTime(Integer oid, Integer rid, Date date);

    public boolean addParticipants(Integer uid,Integer oid,Integer reserveInfoId,Integer[] uids);

    public boolean deleteParticipants(Integer oid,Integer reserveInfoId);
}
