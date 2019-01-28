package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;

import java.util.Date;

public interface ReserveInfoService {

    public boolean addReserveInfo(ReserveInfo reserveInfo);

    public boolean deleteReserveInfo(Integer oid,Integer reserveId);

    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    public ReserveInfo getReserveInfoByReserveId(Integer oid,Integer reserveId);

    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date);

    public ReserveInfo[] queryIsAvailable(Integer rid,String beginTime,String endTime);

    public Integer queryIsAvailableByReserveId(Integer reserveId,String beginTime,String endTime);
}
