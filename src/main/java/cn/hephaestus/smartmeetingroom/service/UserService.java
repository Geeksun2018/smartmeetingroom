package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public Boolean login(String userName,String password);

    public User findUserByUserName(String userName);

    public User getUserByUserName(String userName);

    public Boolean logout();

    public void register(User user);

    public boolean saveUserHeadPortrait(MultipartFile multipartFile, Integer id);


    public boolean alterUserInfo(Integer id,UserInfo userInfo);

    public UserInfo getUserInfo(Integer id);
}
