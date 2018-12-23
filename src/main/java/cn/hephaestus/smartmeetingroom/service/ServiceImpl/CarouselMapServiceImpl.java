package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.CarouselMapMapper;
import cn.hephaestus.smartmeetingroom.model.CarouselMap;
import cn.hephaestus.smartmeetingroom.service.CarouselMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarouselMapServiceImpl implements CarouselMapService {
    @Autowired
    CarouselMapMapper carouselMapMapper;
    @Override
    public boolean addCarouselMap(CarouselMap carouselMap) {
        return carouselMapMapper.addCarouselMap(carouselMap);
    }

    @Override
    public boolean deleteCarouselMap(int id, int oid) {
        return carouselMapMapper.deleteCarouselMap(id,oid);
    }

    @Override
    public boolean alterCarouselMap(CarouselMap carouselMap) {
        return carouselMapMapper.alterCarouselMap(carouselMap);
    }

    @Override
    public CarouselMap[] getCarouselMap(int oid) {
        return carouselMapMapper.getAllCarouselMap(oid);
    }
}
