package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.model.Message;
import cn.hephaestus.smartmeetingroom.service.NewsService;
import cn.hephaestus.smartmeetingroom.websocket.MessageSocketServer;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {


    @Override
    public boolean sendNews(String news, Integer[] idArr) {
        Message message=new Message();
        message.setContent(news);
        message.setType(Message.NEWS);
        message.setInformType("系统消息");
        MessageSocketServer.sentAll(idArr,message.toString());
        return true;
    }
}
