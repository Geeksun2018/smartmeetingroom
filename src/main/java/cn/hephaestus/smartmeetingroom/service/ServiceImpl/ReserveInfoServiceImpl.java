package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.ReserveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {

    @Autowired
    private ReserveTableMapper reserveTableMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean addReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.addReserveInfo(reserveInfo);
    }

    @Override
    public boolean deleteReserveInfo(Integer oid,Integer reserveId) {
        if(reserveTableMapper.deleteReserveInfo(reserveId,oid)){
            redisService.del(oid + "cm" + reserveId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateReserveInfo(ReserveInfo reserveInfo) {
        return reserveTableMapper.updateReserveInfo(reserveInfo);
    }

    @Override
    public ReserveInfo getReserveInfoByReserveId(Integer oid,Integer reserveId) {
        return reserveTableMapper.getReserveInfoByReserveId(oid,reserveId);
    }

    @Override
    public ReserveInfo[] getReserveInfoByRoomId(Integer rid, Date date) {
        return reserveTableMapper.getReserveInfoByRoomId(rid,date);
    }

    @Override
    public ReserveInfo[] queryIsAvailable(Integer rid, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvailable(rid,beginTime,endTime);
    }

    @Override
    public Integer queryIsAvailableByReserveId(Integer reserveId, String beginTime, String endTime) {
        return reserveTableMapper.queryIsAvailableByReserveId(reserveId,beginTime,endTime);
    }
}
