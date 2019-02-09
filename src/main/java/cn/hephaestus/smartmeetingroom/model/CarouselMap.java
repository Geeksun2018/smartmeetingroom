package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarouselMap {
    private int id;
    //所属于组织
    private int oid;
    //图片路径
    private String imagePath;
    //链接地址
    private String link;

    private String title;
    //开始展示时间
    private String startTime;
    //结束展示时间
    private String endTime;
}
