package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.entity.UserInfoEntity;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public Boolean login(String userName,String password);

    public User getUserByUserId(Integer Id);

    public User getUserByUserName(String userName);

    public Boolean logout();

    //注册一个普通账号，供app用户使用
    public void register(User user);

    //注册一个企业（组织）管理账号，供web管理平台使用
    public boolean registerForOrganization(User user);

    public boolean saveUserHeadPortrait(MultipartFile multipartFile, Integer id);


    public boolean alterUserInfo(UserInfo userInfo);

    public UserInfo getUserInfo(Integer id);

    public boolean setOid(Integer oid,Integer userId);

    public boolean setDid(Integer did,Integer userId);

    public UserInfoEntity getUserInfoEntity(Integer id);

    public boolean setFid(Integer fid,Integer userId);

    public List<UserInfo> getUserinfoListByOid(Integer oid);

    public List<UserInfo> getUserInfoListByDid(Integer oid,Integer did);

    public String getHeadPortrait(Integer uid);

    public Integer[] getAllUserByDeparment(int oid,int did);
    //获取预定权限
    public int getReserveJurisdiction(Integer id);
    //修改预定权限
    public boolean alterReserveJurisdiction(Integer jurisdiction,Integer id);

    public boolean addDuerosAcount(Integer uid,String deviceId);

    public Integer[] getAllUserIdByOrganization( Integer oid);
}
