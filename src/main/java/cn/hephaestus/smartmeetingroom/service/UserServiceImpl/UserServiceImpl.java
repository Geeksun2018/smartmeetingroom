package cn.hephaestus.smartmeetingroom.service.UserServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.UserMapper;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.Md5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
        }catch(Exception e){
            return false;
        }

        return true;
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public void register(User user)
    {
        String password = user.getPassword();
        String salt = produceSalt();
        password = password + salt;
        String Md5Password = Md5Util.MD5Encode(password, "UTF-8", true);
        user.setPassword(Md5Password);
        user.setSalt(salt);
        user.setId(userMapper.register(user));
    }

    public String produceSalt()
    {
        StringBuilder randomString= new StringBuilder();
        Random random = new Random();

        for(int i = 0;i < SALT_LENGTH;i++)
        {
            String strRand = null;
            strRand = String.valueOf(codeSequence[random.nextInt(62)]);
            randomString.append(strRand);
        }
        return randomString.toString();
    }

}
