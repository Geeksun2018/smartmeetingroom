package cn.hephaestus.smartmeetingroom.service.UserServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.UserMapper;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

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

}
