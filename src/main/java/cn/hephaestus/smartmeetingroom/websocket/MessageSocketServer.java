package cn.hephaestus.smartmeetingroom.websocket;

import cn.hephaestus.smartmeetingroom.model.Message;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import cn.hephaestus.smartmeetingroom.service.ServiceImpl.MeetingParticipantServiceImpl;
import cn.hephaestus.smartmeetingroom.service.ServiceImpl.RedisServiceImpl;
import cn.hephaestus.smartmeetingroom.service.ServiceImpl.UserServiceImpl;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import cn.hephaestus.smartmeetingroom.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/getMessageServer/{token}")
@Component
public class MessageSocketServer {

    private static ConcurrentHashMap<Integer,MessageSocketServer> webSocketMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer,List<String>> waitToSent=new ConcurrentHashMap<>();

    private Session session;
    private Integer id;
    private UserInfo userInfo;

    //发送给所有的用户
    public static void sentAll(String message){
        Set<Integer> set=webSocketMap.keySet();
        MessageSocketServer messageSocketServer=null;
        for (Integer it:set){
            if(it != null){
                messageSocketServer=webSocketMap.get(it);
                messageSocketServer.session.getAsyncRemote().sendText(message);
            }
        }
    }

    //发送给指定的用户集合
    public static void sentAll(Integer[] idArr,String message){
        MessageSocketServer messageSocketServer=null;
        for (int i=0;i<idArr.length;i++){
            if(idArr[i] == null){
                continue;
            }
            messageSocketServer=webSocketMap.get(idArr[i]);
            if (messageSocketServer==null){
                //如果当前用户不在线，则放在缓存列表中。。。。。。
                List<String> list=waitToSent.get(idArr[i]);
                if (list==null){
                    waitToSent.put(idArr[i],new LinkedList<>());
                    waitToSent.get(idArr[i]).add(message);
                }else {
                    list.add(message);
                }
                continue;
            }
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }


    //接受新的连接，并判断权限是否足够
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) throws IOException {
        this.session=session;
        Integer id=JwtUtils.getId(token);
        UserService userService= SpringUtil.getBean(UserServiceImpl.class);
        RedisService redisService = SpringUtil.getBean(RedisServiceImpl.class);
        MeetingParticipantService meetingParticipantService = SpringUtil.getBean(MeetingParticipantServiceImpl.class);
        if (id!=null){
            this.id=id;
            userInfo = userService.getUserInfo(id);
            webSocketMap.put(id,this);//有新的连接，加入map中
            //判断又没有该用户的信息，如果有就发送
            List<String> list = null;
            if(waitToSent.get(id) != null){
                list = waitToSent.get(id);//获取信息
            }else{
                list = new LinkedList<>();
            }
            Integer did = userInfo.getDid();
            //查询所有私发给该用户的信息kind=person
            Set<String> messages = redisService.sget("person" + id);
            if(messages.size() != 0){
                list.addAll(messages);
            }
            //查询所有通知该部门的信息
            messages = redisService.sget("department" + did);
            if(messages.size() != 0){
                list.addAll(messages);
            }
            //查询所有会议的通知信息
            Set<String>meetingId = redisService.sGetByPattern("meeting");
            for(String meeting:meetingId){
                String midStr = meeting.substring(7);
                Integer mid = Integer.parseInt(midStr);
                Integer[] participants = meetingParticipantService.getParticipants(did,mid);
                for(Integer pid:participants){
                    if(pid.equals(id)){
                        list.addAll(redisService.sget("meeting" + mid));
                        break;
                    }
                }
            }
            String message;
            if (list.size()!=0){
                removeDuplicate(list);
                for (int i=0;i<list.size();i++){
                    message=list.get(i);
                    if (message!=null){
                        session.getAsyncRemote().sendText(message);
                    }
                    list.remove(i);
                    i--;
                }
            }
            LogUtils.getBussinessLogger().info("有新的连接"+id);
        }else {
            session.close();
        }
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(this.id);//连接断开，移除session
        this.session.close();
        LogUtils.getBussinessLogger().info("一个连接断开"+this.id);
    }

    //接受来自客户端的消息，服务器起转发作用
    @OnMessage
    public void onMessage(String message) throws IOException {
        UserService userService= SpringUtil.getBean(UserServiceImpl.class);
        RedisService redisService = SpringUtil.getBean(RedisServiceImpl.class);
        ObjectMapper objectMapper=new ObjectMapper();
        Message m=null;

        try {
            m=objectMapper.readValue(message,Message.class);
            m.getReciveId();
        }catch (Exception e){
            return;
        }

        if (m.getType().equals(Message.PERSON)||m.getInformType().equals(Message.PERSON)){//个人对个人
            sendMessage(m.getReciveId(),m.toString());
            if(m.getExpire() != 0){
                redisService.sadd(m.getInformType() + m.getReciveId(),message);
                redisService.expire(m.getInformType() + m.getReciveId(),m.getExpire() * 3600);
            }
        }else if (m.getType().equals(Message.MEETING)||m.getInformType().equals(Message.MEETING)){
            Set<String> set=redisService.sget(m.getReciveId().toString());
            List<Integer> list=new LinkedList<>();
            for (String s:set){
                list.add(Integer.parseInt(s));
            }
            Integer[] idArray= (Integer[]) list.toArray();
            sentAll(idArray,message);
            if(m.getExpire() != 0){
                redisService.sadd(m.getInformType() + m.getReciveId(),message);
                redisService.expire(m.getInformType() + m.getReciveId(),m.getExpire() * 3600);
            }
        }else if (m.getType().equals(Message.DEPARTMENT)||m.getInformType().equals(Message.DEPARTMENT)){//发送给整个部门
            if (userInfo==null){
                userInfo=userService.getUserInfo(id);
            }
            Integer[] integers=userService.getAllUserByDeparment(userInfo.getOid(),userInfo.getDid());
            for(int i = 0;i < integers.length;i++){
                if(integers[i].equals(id)){
                    integers[i] = null;
                }
            }
            sentAll(integers,message);//发送
            if(m.getExpire() != 0){
                redisService.sadd(m.getInformType() + userInfo.getDid(),message);
                redisService.expire(m.getInformType() + userInfo.getDid(),m.getExpire() * 3600);
            }
        }else if(m.getType().equals(Message.NEWS)&&m.getInformType().equals(Message.DEPARTMENT)){//给整个部门发送通知
            Integer did = m.getReciveId();
            //此处发送者只能给自己公司的部门发送通知
            userInfo = userService.getUserInfo(m.getSentId());
            Integer[] integers=userService.getAllUserByDeparment(userInfo.getOid(),did);
            sentAll(integers,message);//发送
            //这里并没有将通知的内容放进缓存，暂时认为通知只会给用户发送一次

        }
        LogUtils.getBussinessLogger().info("发送了消息"+m.toString());
    }

    //发送消息
    public void sendMessage(Integer id,String message) throws IOException {
        MessageSocketServer messageSocketServer = webSocketMap.get(id);
        if(messageSocketServer==null){
            List<String> list = waitToSent.get(id);
            if(list == null){
                waitToSent.put(id,new LinkedList<>());
                waitToSent.get(id).add(message);
            }
        }else{
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }

    private List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

}

