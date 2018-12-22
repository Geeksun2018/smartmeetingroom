package cn.hephaestus.smartmeetingroom.websocket;

import cn.hephaestus.smartmeetingroom.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


@ServerEndpoint("/getMessageServer/{token}")
@Component
public class MessageSocketServer {
    private static HashMap<Integer,MessageSocketServer> webSocketMap = new HashMap<>();

    private Session session;
    private Integer id;

    //发送给所有的用户
    public static void sentAll(String message){
        Set<Integer> set=webSocketMap.keySet();
        MessageSocketServer messageSocketServer=null;
        for (Integer it:set){
            messageSocketServer=webSocketMap.get(it);
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }

    //发送给指定的用户
    public static void sentAll(Integer[] idArr,String message){
        for (int i=0;i<idArr.length;i++){
            webSocketMap.get(idArr[i]).session.getAsyncRemote().sendText(message);
        }
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) throws IOException {
        this.session=session;
        Integer id=JwtUtils.getId(token);
        if (id!=null){
            this.id=id;
            webSocketMap.put(id,this);//有新的连接，加入map中
            System.out.println("有新的连接"+id);
        }else {
            session.close();
        }
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(this.id);//连接断开，移除session
        this.session.close();
        System.out.println("一个连接断开"+this.id);
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        Message m=objectMapper.readValue(message,Message.class);
        m.getReciveId();
        webSocketMap.get(m.getReciveId()).session.getBasicRemote().sendText(m.toString());
        System.out.println("发送了消息"+m.toString());
    }

    public void sendMessage(Integer id,String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}

/**
 * 定义一条消息
 */
@Setter
@Getter
@AllArgsConstructor
class Message{
    public Message() {
    }

    public Integer sentId;//发送者id
    public Integer reciveId;//接受着id
    public String secretKey;//秘钥，使用非对称算法加密
    public String type;//种类
    public String content;//内容
    public long time;//发送时

    public String toString(){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{}";
    }
}
