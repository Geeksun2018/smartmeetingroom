package cn.hephaestus.smartmeetingroom.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JwtUtils {

    private Logger logger= LoggerFactory.getLogger(JwtUtils.class);

    //token秘钥,不能泄露
    private static final String SECRET="zxh18100738792";

    public static final int calendarField= Calendar.DATE;

    public static  final int calendarInterval=7;//过期时间,一周

    //生成token
    public static String createToken(UUID uuid,String id) throws Exception{
        Date iatDate=new Date();
        Calendar nowTime=Calendar.getInstance();
        nowTime.add(calendarField,calendarInterval);
        Date expiresDate=nowTime.getTime();//过期时间

        //头部信息
        Map<String,Object> map=new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");

        String token= JWT.create().withHeader(map).withClaim("iss","Service")
                .withClaim("aud","APP").withClaim("uuid",uuid.toString())
                .withClaim("id",id)
                .withIssuedAt(iatDate)
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    //解密token
    public  static Map<String, Claim> VerifyToken(String token){
        DecodedJWT jwt=null;
        try {
            JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt=verifier.verify(token);
        }catch (Exception e){
            //校验失败
        }
        if (jwt==null){
            return null;
        }
        return jwt.getClaims();
    }

    public static Integer getId(String token){
        Map<String,Claim> map=JwtUtils.VerifyToken(token);
        return Integer.valueOf(map.get("id").asString());
    }

    public static String getUID(String token){
        Map<String,Claim> map=JwtUtils.VerifyToken(token);
        return map.get("uuid").asString();
    }


}
