package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.entity.UserInfoEntity;
import cn.hephaestus.smartmeetingroom.mapper.UserInfoMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserMapper;
import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import cn.hephaestus.smartmeetingroom.service.OrganizationService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.COSUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;//盐值长度
    final int ENCRYPT_NUM=1024;//加密次数

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private DepartmentService departmentService;


    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean logout(){
        Subject currentUser=SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()){
            currentUser.logout();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User getUserByUserId(Integer id) {
        return userMapper.getUserByUserId(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }


    @Override
    public void register(User user)
    {
        String password = user.getPassword();
        String salt=produceSalt();//生成八位的盐值
        ByteSource byteSource=ByteSource.Util.bytes(salt);
        SimpleHash simpleHash=new SimpleHash("md5",password,byteSource,ENCRYPT_NUM);
        user.setPassword(simpleHash.toHex());
        user.setSalt(salt);
        userMapper.register(user);
    }

    @Override
    public boolean registerForOrganization(User user) {
        //注册根用户
        user.setRole(2);
        this.register(user);
        //注册企业
        OrganizationInfo organizationInfo=new OrganizationInfo();
        organizationInfo.setRootId(user.getId());
        organizationService.addOne(organizationInfo);
        //用户所属组织
        this.setOid(organizationInfo.getId(),user.getId());
        return true;
    }

    @Override
    public boolean alterUserInfo(UserInfo userInfo) {
        return userInfoMapper.alterUserInfo(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Integer id) {
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        return userInfo;
    }

    @Override
    public boolean setOid(Integer oid, Integer userId) {
        return userInfoMapper.setOriganization(oid,userId);
    }

    @Override
    public boolean setDid(Integer did, Integer userId) {
//        return userInfoMapper.setDepartment(did,userId);
    return false;
    }

    @Override
    public UserInfoEntity getUserInfoEntity(Integer id) {
        String departmentName=null;
        String orgName=null;
        User user = userMapper.getUserByUserId(id);
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        if(userInfo == null){
            return null;
        }
        if (userInfo.getDid()!=null&&userInfo.getOid()!=null){
            departmentName = departmentService.getDepartment(userInfo.getOid(),userInfo.getDid()).getDepartmentName();
            orgName= organizationService.getOne(userInfo.getOid()).getOrgName();
        }

        UserInfoEntity userInfoEntity = new UserInfoEntity(userInfo.getId(),userInfo.getSex(),userInfo.getPhoneNum(),userInfo.getEmail(),userInfo.getImagePath()
                ,userInfo.getName(),userInfo.getNickName(),orgName,departmentName,user.getRole(),userInfo.getOid(),userInfo.getDid());
        return userInfoEntity;
    }

    @Override
    public boolean setFid(Integer fid, Integer userId) {
        return userInfoMapper.setFaceFeatureData(fid,userId);
    }

    @Override
    public List<UserInfo> getUserinfoListByOid(Integer oid) {
        return userInfoMapper.getUserinfoListByOid(oid);
    }

    @Override
    public List<UserInfo> getUserInfoListByDid(Integer oid,Integer did) {
        return userInfoMapper.getUserinfoListByDid(oid,did);
    }

    @Override
    public String getHeadPortrait(Integer uid) {
        return userInfoMapper.getHeadPortrait(uid);
    }

    @Override
    public Integer[] getAllUserByDeparment(int oid, int did) {
        return userInfoMapper.getAllUserIdByDepartment(oid,did);
    }

    @Override
    public int getReserveJurisdiction(Integer id) {
        return userMapper.getReserveJurisdiction(id);
    }

    @Override
    public boolean alterReserveJurisdiction(Integer jurisdiction, Integer id) {
        return userMapper.alterReserveJurisdiction(jurisdiction,id);
    }

    @Override
    public boolean addDuerosAcount(Integer uid, String deviceId) {
        return userMapper.addDuerosAccount(uid,deviceId);
    }

    @Override
    public Integer[] getAllUserIdByOrganization(Integer oid) {
        return userInfoMapper.getAllUserIdByOrganization(oid);
    }

    @Override
    public boolean saveUserHeadPortrait(MultipartFile multipartFile, Integer id) {
        String username=userMapper.getUserByUserId(id).getUsername();
        if (username==null){
            return false;
        }
        InputStream inputStream=null;
        String url=null;
        try {
            inputStream=multipartFile.getInputStream();
            url=saveUserHeadPortrait(inputStream,username);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (url!=null) {
            return userInfoMapper.alterHeadPortrait(id, url);
        }
        return false;
    }

    private  String saveUserHeadPortrait(InputStream inputStream,String username){
        try {
            String url= COSUtils.addFile("head_portrait/"+username+"_headportrait" + UUID.randomUUID(),inputStream);
            return url;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
