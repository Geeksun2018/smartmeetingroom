package cn.hephaestus.smartmeetingroom.model;

public class UserFaceInfo {
    private Integer faceInfoId;
    private byte[] featureData;

    public Integer getFaceInfoId() {
        return faceInfoId;
    }

    public void setFaceInfoId(Integer faceInfoId) {
        this.faceInfoId = faceInfoId;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }
}
