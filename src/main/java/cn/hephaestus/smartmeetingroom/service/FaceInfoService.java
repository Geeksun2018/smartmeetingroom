package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;
import cn.hephaestus.smartmeetingroom.model.UserInfo;

import java.util.List;

public interface FaceInfoService {

    public boolean addFaceInfo(UserFaceInfo userFaceInfo);

    public boolean deleteFaceInfo(Integer faceInfoId);

    public boolean updateFaceInfo(UserFaceInfo userFaceInfo);

    public UserFaceInfo getFaceInfo(Integer faceInfoId);

    public List<UserFaceInfo> getUserFaceInfoList(List<UserInfo> lists);

}
