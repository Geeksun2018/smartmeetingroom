package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.CarouselMap;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.CarouselMapService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.COSUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
public class CarouselMapController {
    @Autowired
    CarouselMapService carouselMapService;

    @Autowired
    UserService userService;
    @RequestMapping("/addCarouselMap")
    public RetJson addCarouselMap(CarouselMap carouselMap, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request){
        User user= (User) request.getAttribute("user");
        if (user.getRole()!=2){
            return RetJson.fail(-2,"权限不足!");
        }
        UserInfo userInfo=userService.getUserInfo(user.getId());

        //文件上传获取链接
        InputStream inputStream=null;
        try {
            inputStream=multipartFile.getInputStream();
        }catch (Exception e){
            return RetJson.fail(-1,"上传失败!");
        }

        String url=COSUtils.addFile("carousel_map/"+userInfo.getOid()+"/"+multipartFile.getOriginalFilename(),inputStream);
        carouselMap.setLink(url);
        carouselMap.setOid(userInfo.getOid());

        if (carouselMapService.addCarouselMap(carouselMap)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"添加失败");
    }

    @RequestMapping("/deleteCarouselMap")
    public RetJson deleteCarouselMap(int id, HttpServletRequest request){
        User user= (User) request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        if (user.getRole()==2) {
            if (carouselMapService.deleteCarouselMap(id, userInfo.getOid())) {
                return RetJson.succcess(null);
            }
        }
        return RetJson.fail(-1,"删除失败!");
    }

    @RequestMapping("alterCarouselMap")
    public RetJson alterCarouselMap(int id,CarouselMap carouselMap,HttpServletRequest request){
        User user= (User) request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        if (user.getRole()==2){
            carouselMap.setOid(userInfo.getOid());
            if (carouselMapService.alterCarouselMap(carouselMap)){
                return RetJson.succcess(null);
            }
        }
        return RetJson.fail(-1,"修改失败!");
    }

    @RequestMapping("getAllCarouselMap")
    public RetJson getAllCarouselMap(HttpServletRequest request){
        User user= (User) request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        if (user.getRole()==2){
            CarouselMap[] carouselMaps=carouselMapService.getCarouselMap(userInfo.getOid());
            return RetJson.succcess("carouselMapList",carouselMaps);
        }
        return RetJson.fail(-1,"获取失败");
    }


}
