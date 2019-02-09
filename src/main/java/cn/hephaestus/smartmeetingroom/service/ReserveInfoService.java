package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;

import java.util.Date;
import java.util.List;

public interface ReserveInfoService {

    public boolean addReserveInfo(ReserveInfo reserveInfo);

    public boolean deleteReserveInfo(Integer oid,Integer reserveId);

    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    public ReserveInfo getReserveInfoByReserveId(Integer oid,Integer reserveId);

    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date);

    public ReserveInfo[] queryIsAvailable(Integer rid,String beginTime,String endTime);

    public Integer queryIsAvailableByReserveId(Integer reserveId,String beginTime,String endTime);

    public List<ReserveInfoViewObject> getReserveInfoViewObjectByCondition(Date date, Integer rid, Integer did,Integer oid);
}
