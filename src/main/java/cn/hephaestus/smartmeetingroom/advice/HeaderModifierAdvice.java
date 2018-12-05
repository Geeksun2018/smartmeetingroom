package cn.hephaestus.smartmeetingroom.advice;

import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.UserServiceImpl.RedisServiceImpl;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
@ControllerAdvice
public class HeaderModifierAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    RedisService redisService;
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //参数转换
        ServletServerHttpResponse ssResp = (ServletServerHttpResponse)serverHttpResponse;
        ServletServerHttpRequest ssReq = (ServletServerHttpRequest)serverHttpRequest;
        HttpServletResponse resp=ssResp.getServletResponse();
        HttpServletRequest res=ssReq.getServletRequest();

        //判断是否是login方法,对login方法做特殊处理
        if (methodParameter.getMethod().getName().equals("login")){
            String id=Integer.toString((Integer) res.getAttribute("id"));
            String lastUserUid=redisService.hget("curent_u",id);
            if (lastUserUid!=null){//之前已经有用户登入了,被挤下线
                redisService.remove(lastUserUid);
            }
            //设置当前在线用户
            String uuid=(String) res.getAttribute("uuid");redisService.hset("curent_u",id,uuid);
            return o;
        }


        //获取旧的uuid
        String uuid=(String) res.getAttribute("uuid");
        //获取用户id
        String id=(String)res.getAttribute("id");

        String newToken=null;
        try{
            //获取新的token
            UUID newUUID=UUID.randomUUID();
            newToken=JwtUtils.createToken(newUUID);
            if (newToken!=null){
                //删除原来的token
                if (redisService.exists(uuid)){
                    redisService.remove(uuid);
                }
                //添加新的token
                resp.addHeader("Authorization", newToken);
                //更新redis中的token
                redisService.set(newUUID.toString(),id);
                redisService.hset("curent_u",id,newUUID.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }
}
