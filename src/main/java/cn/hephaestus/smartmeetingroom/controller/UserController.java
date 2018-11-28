package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.GenerateVerificationCode;
import cn.hephaestus.smartmeetingroom.utils.MoblieMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeng
 * 与用户操作有关的控制器,如登入注册等
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
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

    @RequestMapping("/getcode")
    public RetJson sendIdentifyingCode(@RequestParam(value = "phonenumber") String phoneNumber){
        String verificationCode = GenerateVerificationCode.generateVerificationCode(4);
        SendSmsResponse response = null;
        try {
            response = MoblieMessageUtil.sendIdentifyingCode(phoneNumber, verificationCode);
        }catch (ClientException e){
            e.printStackTrace();
        }
        String code = response.getCode();
        String message = response.getMessage();
        String requestId = response.getRequestId();
        String bizId = response.getBizId();
        //在redis中存入用户的账号和对应的验证码
        redisService.set(phoneNumber,verificationCode,300);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + code);
        System.out.println("Message=" + message);
        System.out.println("RequestId=" + requestId);
        System.out.println("BizId=" + bizId);

        if(code!=null&&code.equals("OK")){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,message);
    }


}
