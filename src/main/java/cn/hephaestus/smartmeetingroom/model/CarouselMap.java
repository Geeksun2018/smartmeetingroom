package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarouselMap {
    private int id;
    private int oid;//所属于组织
    private String imagePath;//图片路径
    private String link;//链接地址
    private String title;
    private String startTime;//开始展示时间
    private String endTime;//结束展示时间
}
