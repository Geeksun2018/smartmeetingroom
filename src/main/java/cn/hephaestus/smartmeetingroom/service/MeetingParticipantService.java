package cn.hephaestus.smartmeetingroom.service;

public interface MeetingParticipantService {
    public int addParticant(Integer reserveInfoId,Integer uid);

    public boolean deleteParticant(Integer reserveInfoId,Integer uid);

    public Integer[] getParticipants(Integer reserveInfoId);

    public boolean addParticipants(Integer reserveInfoId,Integer[] uids);

    public boolean deleteParticipants(Integer reserveInfoId);
}
