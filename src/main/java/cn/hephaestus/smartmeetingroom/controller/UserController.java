package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.GenerateVerificationCode;
import cn.hephaestus.smartmeetingroom.utils.MoblieMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeng
 * 与用户操作有关的控制器,如登入注册等
 */
@RestController
@Validated
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
    public RetJson sendIdentifyingCode(@Length(max = 11, min = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "phonenumber") String phoneNumber){
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

    @RequestMapping("/register")
    public RetJson userRegister(@Length(min = 11,max = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "username") String phoneNumber
            ,String password,String code){
        if(redisService.exists(phoneNumber)&&redisService.get(phoneNumber).equals(code)){
            if(userService.findUserByUserName(phoneNumber) == null){
                User user = new User();
                user.setUserName(phoneNumber);
                user.setPassword(password);
                userService.register(user);
                return RetJson.succcess(null);
            }
            return RetJson.fail(-1,"用户已存在！");
        }
        return RetJson.fail(-1,"验证码不正确！");
    }
}
