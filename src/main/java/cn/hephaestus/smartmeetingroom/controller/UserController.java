package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeng
 * 与用户操作有关的控制器,如登入注册等
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    //登入
    @RequestMapping("/login")
    public RetJson login(User user){
        Boolean b=userService.login(user.getUserName(),user.getPassword());
        if (b==true){
            return RetJson.succcess(null);
        }else {
            return RetJson.fail(-1,"登入失败,请检查用户名或密码");
        }
    }
}
