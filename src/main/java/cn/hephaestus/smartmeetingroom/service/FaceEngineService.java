package cn.hephaestus.smartmeetingroom.service;

import com.arcsoft.face.FaceFeature;


public interface FaceEngineService {


    Integer compareFaceFeature(FaceFeature targetFaceFeature, FaceFeature sourceFaceFeature) throws Exception;

}
