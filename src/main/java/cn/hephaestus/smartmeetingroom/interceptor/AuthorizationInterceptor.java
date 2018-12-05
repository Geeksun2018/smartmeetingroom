package cn.hephaestus.smartmeetingroom.interceptor;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserServiceImpl.RedisServiceImpl;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Map;

/**
 * @author zeng
 * 权限认证,如果失败,则返回授权失败信息
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisService redisService;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头部中的token
        String token=request.getHeader("Authorization");
        if (token==null){
            Writer writer=response.getWriter();
            writer.write(RetJson.fail(-2,"token已过期,请重新登入").toString());
            writer.flush();
            return false;
        }
        //解密token
        Map<String, Claim> map=JwtUtils.VerifyToken(token);
        String uuid=map.get("uuid").asString();

        //判断token是否有效
        if (uuid!=null&&redisService.exists(uuid)){
            String id=(String) redisService.get(uuid);
            request.setAttribute("id",id);
            request.setAttribute("uuid",uuid);
        }else {
            //否则提示token过期,要求重新登录
            Writer writer=response.getWriter();
            writer.write(RetJson.fail(-2,"token已过期,请重新登入").toString());
            writer.flush();
            return false;
        }
        return true;
    }
}
