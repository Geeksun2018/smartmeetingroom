package cn.hephaestus.smartmeetingroom.service.UserServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.UserInfoMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserMapper;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.COSUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;//盐值长度
    final int ENCRYPT_NUM=1024;//加密次数

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();


        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
        }catch(Exception e){
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
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
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
        user.setId(userMapper.register(user));
    }

    @Override
    public boolean alterUserInfo(Integer id, UserInfo userInfo) {
        //判断是否为本人操作
        if(id == userInfo.getId()){
            userInfoMapper.alterUserInfo(id,userInfo);
            return true;
        }
        return false;
    }

    @Override
    public UserInfo getUserInfo(Integer id) {
        return userInfoMapper.getUserInfoById(id);
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
            String url= COSUtils.addFile("head_portrait/"+username+"_headportrait",inputStream);
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
