package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.CarouselMap;

public interface CarouselMapService {
    //添加轮播图
    public boolean addCarouselMap(CarouselMap carouselMap);

    //删除轮播图
    public boolean deleteCarouselMap(int id,int oid);

    //修改轮播图信息
    public boolean alterCarouselMap(CarouselMap carouselMap);

    //获取当天的轮播图
    public CarouselMap[] getCarouselMap(int oid);
}
