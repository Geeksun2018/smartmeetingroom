package cn.hephaestus.smartmeetingroom.service;

public interface MeetingParticipantService {
    public int addParticipant(Integer oid,Integer reserveInfoId,Integer uid);

    public boolean deleteParticipant(Integer oid,Integer reserveInfoId,Integer uid);

    public Integer[] getParticipants(Integer oid,Integer reserveInfoId);

    public boolean addParticipants(Integer oid,Integer reserveInfoId,Integer[] uids);

    public boolean deleteParticipants(Integer oid,Integer reserveInfoId);
}
