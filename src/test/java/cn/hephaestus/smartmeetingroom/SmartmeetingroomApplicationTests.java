package cn.hephaestus.smartmeetingroom;

import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.GenerateVerificationCode;
import cn.hephaestus.smartmeetingroom.utils.MoblieMessageUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartmeetingroomApplicationTests {
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    @RequestMapping("/getcode")
    public void sendIdentifyingCode(){
        String verificationCode = GenerateVerificationCode.generateVerificationCode(4);
        SendSmsResponse response = null;
        try {
            response = MoblieMessageUtil.sendIdentifyingCode("*********", verificationCode);
        }catch (ClientException e){
            e.printStackTrace();
        }
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    }

    @Test
    public void saveData(){
        //redisService.set("18934698676","1234");
        System.out.println(redisService.get("18934698676"));
    }

    @Test
    public void register(){
        User user = new User();
        user.setUserName("Geeksun1");
        user.setPassword("sunwang1");
        userService.register(user);
        System.out.println(user.getId());
    }
}
