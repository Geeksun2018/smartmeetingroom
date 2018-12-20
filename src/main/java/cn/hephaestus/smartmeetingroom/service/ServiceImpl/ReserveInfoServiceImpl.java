package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.ReserveTableMapper;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.service.ReserveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {

    @Autowired
    ReserveTableMapper reserveTableMapper;

    @Override
    public int addReserveInfo(ReserveInfo reserveInfo) {
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
    public ReserveInfo getReserveInfoByRoomId(Integer rid) {
        return reserveTableMapper.getReserveInfoByRoomId(rid);
    }
}
