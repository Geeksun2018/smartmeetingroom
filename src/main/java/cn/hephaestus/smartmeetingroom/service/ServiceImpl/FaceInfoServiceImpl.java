package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.UserFaceInfoMapper;
import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;
import cn.hephaestus.smartmeetingroom.service.FaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceInfoServiceImpl implements FaceInfoService {
    @Autowired
    UserFaceInfoMapper userFaceInfoMapper;
    @Override
    public void addFaceInfo(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.addFaceInfo(userFaceInfo);
    }

    @Override
    public void deleteFaceInfo(Integer faceInfoId) {
        userFaceInfoMapper.deleteFaceInfo(faceInfoId);
    }

    @Override
    public void updateFaceInfo(UserFaceInfo userFaceInfo) {
        userFaceInfoMapper.updateFaceInfo(userFaceInfo);
    }

    @Override
    public String getFaceInfo(Integer faceInfoId) {
        return userFaceInfoMapper.getFaceInfo(faceInfoId);
    }
}
