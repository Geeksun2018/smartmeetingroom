package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.entity.UserInfoEntity;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.EmailService;
import cn.hephaestus.smartmeetingroom.service.OrganizationService;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.GenerateVerificationCode;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import cn.hephaestus.smartmeetingroom.utils.MoblieMessageUtil;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    OrganizationService organizationService;
    @Autowired
    EmailService emailService;
    //登入
    @RequestMapping("/login")
    public RetJson login(User user, Boolean isRemberMe, HttpServletRequest request){
        if (!ValidatedUtil.validate(user)){
            return RetJson.fail(-1,"登入失败，请检查用户名或密码");
        }
        Boolean b=userService.login(user.getUsername(),user.getPassword());
        if (b==true){
            user=userService.getUserByUserName(user.getUsername());
            request.setAttribute("id",user.getId()+"");
            //登入成功,并且设置了记住我,则发放token
            if (isRemberMe){
                //手机app
                try {
                    //生成一个随机的不重复的uuid
                    UUID uuid=UUID.randomUUID();
                    request.setAttribute("uuid",uuid.toString());
                    String token=JwtUtils.createToken(uuid,user.getId().toString());
                    //将uuid和user以键值对的形式存放在redis中
                    user.setPassword(null);
                    user.setSalt(null);
                    redisService.set(uuid.toString(),user.toString(),60*60*24*7);
                    Map map = new LinkedHashMap();
                    map.put("token",token);
                    map.put("id",user.getId());
                    return RetJson.succcess(map);
                }catch (Exception e){
                    System.out.println("token获取失败");
                }
            }
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("id",user.getId());
            return RetJson.succcess(map);
        }else {
            return RetJson.fail(-1,"登入失败,请检查用户名或密码");
        }
    }

    //获取手机验证码
    @RequiresAuthentication
    @RequestMapping("/getcode")
    public RetJson sendIdentifyingCode(@Length(max = 11, min = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "phonenumber") String phoneNumber,int type){
        if (type==0&&(userService.getUserByUserName(phoneNumber)!=null)){
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
        //在redis中存入用户的账号和对应的验证码
        redisService.set(phoneNumber,verificationCode,300);

        if(code!=null&&code.equals("OK")){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,message);
    }

    //注册
    @RequestMapping("/register")
    public RetJson userRegister(User user, String code, Boolean isOrganization, BindingResult result) {
        if(result.hasErrors()){
            List<ObjectError> ls = result.getAllErrors();
            for (int i = 0; i < ls.size(); i++) {
                System.out.println("error:"+ls.get(i));
            }
        }
        if (!ValidatedUtil.validate(user)) {
            return RetJson.fail(-1, "请检查参数");
        }
//        if (redisService.exists(user.getUsername()) && redisService.get(user.getUsername()).equals(code)) {
            if (true) {
                if (userService.getUserByUserName(user.getUsername()) == null) {
                    if (isOrganization) {
                        userService.registerForOrganization(user);
                    } else {
                        userService.register(user);
                    }
                    return RetJson.succcess(null);
                }
                return RetJson.fail(-1, "用户已存在！");
            }
//        }
        return RetJson.fail(-1, "验证码不正确！");
    }

    //获取用户信息
    @RequestMapping("/getUserinfo")
    public RetJson getUserInfo(Integer id,HttpServletRequest request){
        //因为可能要获取他人的信息，因此这里修改了一下
        //User user=(User)request.getAttribute("user");
        UserInfoEntity userInfo=userService.getUserInfoEntity(id);
        if (userInfo==null){
            return RetJson.fail(-1,"获取用户信息失败");
        }else{
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("userinfo",userInfo);
            return RetJson.succcess(map);
        }
    }
    //修改用户信息
    @RequestMapping("/alterUserinfo")
    public RetJson alterUserInfo(UserInfo userInfo,HttpServletRequest request){
        //将就一下
        if (!ValidatedUtil.validate(userInfo)){
            return RetJson.fail(-1,"请检查参数");
        }
        Integer id= ((User)request.getAttribute("user")).getId();
        userInfo.setId(id);
        userService.alterUserInfo(id,userInfo);//写死
        return RetJson.succcess(null);
    }

    //修改用户头像,涉及到文件上传,单独拿出来
    @RequestMapping("/alterHeadPortrait")
    public RetJson alterHeadPortrait(@RequestParam("photo") MultipartFile multipartFile,HttpServletRequest request){
        //图片校验
        Integer id= ((User)request.getAttribute("user")).getId();
        userService.saveUserHeadPortrait(multipartFile,id);
        return RetJson.succcess(null);
    }

    //验证用户邮箱
    @RequestMapping("/bindMailbox")
    public RetJson bindMailbox(String email,String code){
        String redisCode=(String) redisService.get(email);
        if (code.equals(redisCode)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"邮箱验证码错误");
    }


    //获取邮箱验证码
    @RequestMapping("/getEmailCode")
    public void getEmailCode(@RequestParam("email") String email){
        emailService.sentVerificationCode(email);
    }
}
