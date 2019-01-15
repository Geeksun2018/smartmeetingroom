package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;

import java.util.Date;

public interface ReserveInfoService {

    public boolean addReserveInfo(ReserveInfo reserveInfo);

    public boolean deleteReserveInfo(Integer reserveId);

    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    public ReserveInfo getReserveInfoByReserveId(Integer reserveId);

    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date);

    public ReserveInfo[] queryIsAvaliable(Integer rid,String beginTime,String endTime);

    public Integer queryIsAvaliableByReserveId(Integer reserveId,String beginTime,String endTime);
}
