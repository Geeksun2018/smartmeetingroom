package cn.hephaestus.smartmeetingroom.websocket;

import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import cn.hephaestus.smartmeetingroom.utils.SpringUtil;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/link/{md5}")
@Component
public class AndroidThingsSocketServer {

    private static ConcurrentHashMap<String,AndroidThingsSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;
    private String md5;
    private static MeetingRoomService meetingRoomService;



    //接受新的连接，并判断权限是否足够
    @OnOpen
    public void onOpen(Session session,@PathParam("md5") String md5) throws IOException {
        if (meetingRoomService==null){
            meetingRoomService=SpringUtil.getBean(MeetingRoomService.class);
        }
        if (!meetingRoomService.judgeMeeting(md5)){
            session.close();
            System.out.println("连接失败");
            return;//错误连接，断开
        }
        this.session=session;
        this.md5=md5;
        webSocketMap.put(md5,this);
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(this.md5);//连接断开，移除session
        this.session.close();
    }

    //发送消息
    public static boolean openDoor(String md5){
        AndroidThingsSocketServer socketServer=webSocketMap.get(md5);
        if (socketServer!=null){
            try {
                //发生成功
                socketServer.session.getBasicRemote().sendText("open");
                return true;
            }catch (IOException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openDoor(int rid){
        MeetingRoom meetingRoom=new MeetingRoom();
        if (meetingRoom==null){
            meetingRoom=meetingRoomService.getMeetingRoomWithRoomId(rid);
            if (meetingRoom==null){
                return false;
            }
        }
        return openDoor(meetingRoom.getMacAddress());
    }
}
