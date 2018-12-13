package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;

public interface FaceInfoService {

    public void addFaceInfo(UserFaceInfo userFaceInfo);

    public void deleteFaceInfo(Integer faceInfoId);

    public void updateFaceInfo(UserFaceInfo userFaceInfo);

    public String getFaceInfo(Integer faceInfoId);

}
