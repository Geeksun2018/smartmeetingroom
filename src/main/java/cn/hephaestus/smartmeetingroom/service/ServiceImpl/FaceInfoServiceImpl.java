package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.UserFaceInfoMapper;
import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;
import cn.hephaestus.smartmeetingroom.service.FaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceInfoServiceImpl implements FaceInfoService {
    @Autowired
    private UserFaceInfoMapper userFaceInfoMapper;
    @Override
    public boolean addFaceInfo(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.addFaceInfo(userFaceInfo);
        return true;
    }

    @Override
    public boolean deleteFaceInfo(Integer faceInfoId) {
        userFaceInfoMapper.deleteFaceInfo(faceInfoId);
        return true;
    }

    @Override
    public boolean updateFaceInfo(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.updateFaceInfo(userFaceInfo);
        return true;
    }

    @Override
    public String getFaceInfo(Integer faceInfoId) {
        return userFaceInfoMapper.getFaceInfo(faceInfoId);
    }
}
