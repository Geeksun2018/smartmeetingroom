package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.User;

public interface UserService {
    public Boolean login(String userName,String password);

    public User findUserByUserName(String userName);


}
