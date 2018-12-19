package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;

public interface FaceInfoService {

    public boolean addFaceInfo(UserFaceInfo userFaceInfo);

    public boolean deleteFaceInfo(Integer faceInfoId);

    public boolean updateFaceInfo(UserFaceInfo userFaceInfo);

    public String getFaceInfo(Integer faceInfoId);

}
