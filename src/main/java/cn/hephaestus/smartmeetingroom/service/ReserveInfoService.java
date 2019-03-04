package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public Integer queryCountOfDepartmentMeetingByYear(Integer oid,Integer did,Integer year);

    public Integer queryCountOfDepartmentMeetingByMonth(Integer oid,Integer did,Integer year,Integer month);

    public Integer queryCountOfDepartmentMeetingTimeByYear(Integer oid,Integer did,Integer year);

    public Integer queryCountOfDepartmentMeetingTimeByMonth(Integer oid,Integer did,Integer year,Integer month);

    public Map<Date,Integer> queryCountOfMeetingByDay(Date date);


}
