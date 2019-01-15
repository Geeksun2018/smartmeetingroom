package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.service.ReserveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {

    @Autowired
    ReserveTableMapper reserveTableMapper;

    @Override
    public boolean addReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.addReserveInfo(reserveInfo);
    }

    @Override
    public boolean deleteReserveInfo(Integer reserveId) {
        return reserveTableMapper.deleteReserveInfo(reserveId);
    }

    @Override
    public boolean updateReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.updateReserveInfo(reserveInfo);
    }

    @Override
    public ReserveInfo getReserveInfoByReserveId(Integer reserveId) {
        return reserveTableMapper.getReserveInfoByReserveId(reserveId);
    }

    @Override
    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date) {
        return reserveTableMapper.getReserveInfoByRoomId(rid,date);
    }

    @Override
    public ReserveInfo[] queryIsAvaliable(Integer rid, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvaliable(rid,beginTime,endTime);
    }

    @Override
    public Integer queryIsAvaliableByReserveId(Integer reserveId, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvaliableByReserveId(reserveId,beginTime,endTime);
    }
}
