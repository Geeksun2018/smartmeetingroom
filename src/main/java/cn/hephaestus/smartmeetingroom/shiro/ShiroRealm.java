package cn.hephaestus.smartmeetingroom.shiro;

import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;

import java.util.HashSet;
import java.util.Set;


public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //登入验证逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)token;
        String username=usernamePasswordToken.getUsername();
        User dbuser=userService.findUserByUserName(username);

        if (dbuser==null){
            return null;//登入失败
        }
        String realmNam=getName();
        ByteSource salt = ByteSource.Util.bytes(username);
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(dbuser.getUserName(),dbuser.getPassword(),salt,realmNam);
        return info;
    }

    //权限添加逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal=principalCollection.getPrimaryPrincipal();
        Set<String> roles=new HashSet<>();
        roles.add("user");
        if ("admin".equals(principal)){
            roles.add("admin");
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo(roles);
        return info;
    }

    public static void main(String[] args){
        ByteSource salt = ByteSource.Util.bytes("user");
        SimpleHash simpleHash=new SimpleHash("md5","e10adc3949ba59abbe56e057f20f883e","user");
        System.out.println(simpleHash);
    }
}
