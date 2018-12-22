package cn.hephaestus.smartmeetingroom.interceptor;

import cn.hephaestus.smartmeetingroom.common.RedisSession;
import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.ExcludeURI;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author zeng
 * 权限认证,如果失败,则返回授权失败信息
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisService redisService;

    @Autowired
    ExcludeURI excludeURI;

    @Autowired
    ObjectMapper mapper;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //排除部分url
        String url=request.getRequestURI();
        if (isExclude(url)){
            return true;
        }

        //获取请求头部中的token
        String token=request.getHeader("Authorization");
        if (token!=null){

            //解密token
            Map<String, Claim> map=JwtUtils.VerifyToken(token);
            String uuid=map.get("uuid").asString();
            String id=map.get("id").asString();

            //判断token是否有效
            if (uuid!=null&&id!=null&&redisService.exists("user:"+id)){
                String ret=(String) redisService.get("user:"+id);
                if (ret.equals(uuid)||true){
                    //更新过期时间,连续七天不活动则token失效
                    redisService.expire("user:"+id,60*60*24*7);
                    RedisSession redisSession=RedisSession.getInstance(uuid,Long.valueOf(id));
                    if (redisSession!=null){
                        request.setAttribute("redisSession",redisSession);
                    }
                    //设置在线状态
                    if (redisSession!=null&&!url.equals("/offLine")){
                        redisSession.setUserActiveStatu(true);
                    }
                    return true;
                }else{
                    return false;
                }
            }
        }
        //否则提示token过期,要求重新登录
        Writer writer=response.getWriter();
        writer.write(RetJson.fail(-2,"token已过期,请重新登入").toString());
        writer.flush();
        return false;

    }

    public boolean isExclude(String uri){
        List<String> list=excludeURI.getExcludeuri();
        if (list.contains(uri)){
            return true;
        }
        return false;
    }

}
