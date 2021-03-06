package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.service.EmailService;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean sentVerificationCode(String email) {
        String code=getCode();
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setSubject("Ameeting需要验证你的邮箱");
        mailMessage.setFrom("807920489@qq.com");
        mailMessage.setTo(email);
        mailMessage.setText("你的邮箱验证码为"+code+",十分钟有效");

        try{
            mailSender.send(mailMessage);
        }catch (Exception e){
            LogUtils.getExceptionLogger().error(e.toString());
            return false;
        }
        redisService.set(email,code,60*10);
        return true;
    }

    @Override
    public boolean sentWelcomePage(String email) {
        return false;
    }

    //生成随机的5为验证码
    private static String getCode(){
        String code="";
        Random random=new Random();
        for(int i=1;i<=5;i++){
            code+=random.nextInt(9);
        }
        return code;
    }
}
