package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.NewsMapper;
import cn.hephaestus.smartmeetingroom.model.Message;
import cn.hephaestus.smartmeetingroom.model.News;
import cn.hephaestus.smartmeetingroom.service.NewsService;
import cn.hephaestus.smartmeetingroom.websocket.MessageSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public boolean sendNews(String news, Integer[] idArr) {
        Message message=new Message();
        message.setContent(news);
        message.setType(Message.NEWS);
        //informType的用法似乎有错误，在WebSocket中标记的是发送的对象
        message.setInformType("系统消息");
        MessageSocketServer.sentAll(idArr,message.toString());
        return true;
    }

    @Override
    public boolean insertNews(News news) {
        return newsMapper.insertNews(news);
    }
}
