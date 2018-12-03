package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;

public interface UserService {
    public Boolean login(String userName,String password);

    public User findUserByUserName(String userName);

    public void register(User user);

    public boolean addUserinfo(Integer id,UserInfo userInfo);

    public boolean alterUserInfo(Integer id,UserInfo userInfo);

    public UserInfo getUserInfo(Integer id);
}
