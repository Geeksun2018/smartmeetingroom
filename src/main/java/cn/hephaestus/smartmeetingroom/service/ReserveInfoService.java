package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;

import java.util.Date;
import java.util.List;

public interface ReserveInfoService {

    public boolean addReserveInfo(ReserveInfo reserveInfo);

    public boolean deleteReserveInfo(Integer oid, Integer reserveId);

    public boolean updateReserveInfo(ReserveInfo reserveInfo);

    public ReserveInfo getReserveInfoByReserveId(Integer oid,Integer reserveId);

    public ReserveInfo getReserveInfoByReserveId(Integer uid,Integer oid,Integer reserveId);

    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date);

    public ReserveInfo[] queryIsAvailable(Integer rid,Date beginTime,Date endTime);

    public Integer queryIsAvailableByReserveId(Integer reserveId,Date beginTime,Date endTime);

    public List<ReserveInfoViewObject> getReserveInfoViewObjectByCondition(Date date, Integer rid, Integer did,Integer oid);
}
