package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.GenerateVerificationCode;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import cn.hephaestus.smartmeetingroom.utils.MoblieMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import io.lettuce.core.ScanStream;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
    public RetJson login(User user, Boolean isRemberMe, HttpServletRequest request){
        Boolean b=userService.login(user.getUsername(),user.getPassword());
        if (b==true){
            user=userService.getUserByUserName(user.getUsername());
            request.setAttribute("id",user.getId());
            //登入成功,并且设置了记住我,则发放token
            if (isRemberMe){
                //手机app
                try {
                    //生成一个随机的不重复的uuid
                    UUID uuid=UUID.randomUUID();
                    request.setAttribute("uuid",uuid.toString());
                    String token=JwtUtils.createToken(uuid);
                    //将uuid和id以键值对的形式存放在redis中
                    redisService.set(uuid.toString(),String.valueOf(user.getId()),60*60*24*7);
                    return RetJson.succcess("token",token);
                }catch (Exception e){
                    System.out.println("token获取失败");
                }

            }
            return RetJson.succcess(null);
        }else {
            return RetJson.fail(-1,"登入失败,请检查用户名或密码");
        }
    }

    public static void main(String[] args){
        Integer a=19;
        System.out.println(String.valueOf(19));
    }
    //获取手机验证码
    @RequiresAuthentication
    @RequestMapping("/getcode")
    public RetJson sendIdentifyingCode(@Length(max = 11, min = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "phonenumber") String phoneNumber,int type){
        if (type==0&&(userService.findUserByUserName(phoneNumber)!=null)){
            return RetJson.fail(-1,"该用户已经注册");
        }
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

        if(code!=null&&code.equals("OK")){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,message);
    }

    //注册
    @RequestMapping("/register")
    public RetJson userRegister(@Length(min = 11,max = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "username") String phoneNumber
            ,String password,String code){
        if(redisService.exists(phoneNumber)&&redisService.get(phoneNumber).equals(code)){
            if(userService.findUserByUserName(phoneNumber) == null){
                User user = new User();
                user.setUsername(phoneNumber);
                user.setPassword(password);
                userService.register(user);
                return RetJson.succcess(null);
            }
            return RetJson.fail(-1,"用户已存在！");
        }
        return RetJson.fail(-1,"验证码不正确！");
    }

    //获取用户信息
    @RequestMapping("/getuserinfo")
    public RetJson getUserInfo(){
        UserInfo userInfo=userService.getUserInfo(19);
        if (userInfo==null){
            return RetJson.fail(-1,"获取用户信息失败");
        }else{
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("useriofo",userInfo);
            return RetJson.succcess(map);
        }
    }
    //修改用户信息
    @RequestMapping("/alteruserinfo")
    public RetJson alterUserInfo(UserInfo userInfo){
        userService.alterUserInfo(19,userInfo);//写死
        return RetJson.succcess(null);
    }

    //修改用户头像,涉及到文件上传,单独拿出来
    @RequestMapping("/alterheadPortrait")
    public RetJson alterHeadPortrait(@RequestParam("photo") MultipartFile multipartFile){
        //图片校验
        int id=19;
        userService.saveUserHeadPortrait(multipartFile,id);
        return RetJson.succcess(null);
    }
    @RequestMapping("/test")
    public void test(){
        redisService.set("2","19");
    }

}
