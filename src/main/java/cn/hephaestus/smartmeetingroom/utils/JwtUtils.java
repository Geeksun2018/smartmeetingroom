package cn.hephaestus.smartmeetingroom.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtUtils {
    private Logger logger= LoggerFactory.getLogger(JwtUtils.class);
    private String secret;//秘钥
    private int expire;//有效期限
    private String header;


//    public String generateToken(long userId){
//        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
//
//
//    }

}
