package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.NewsMapper;
import cn.hephaestus.smartmeetingroom.model.Message;
import cn.hephaestus.smartmeetingroom.model.News;
import cn.hephaestus.smartmeetingroom.service.NewsService;
import cn.hephaestus.smartmeetingroom.websocket.MessageSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public boolean sendNews(String news, Integer[] idArr,String informType) {
        Message message=new Message();
        message.setContent(news);
        message.setType(Message.NEWS);
        message.setInformType(informType);
        MessageSocketServer.sentAll(idArr,message.toString());
        return true;
    }

    @Override
    public boolean sendNewsToObject(Message message,Integer[] idArr) {
        if(message.getInformType().equals(Message.DEPARTMENT)){
            MessageSocketServer.sentAll(idArr,message.toString());
            newsMapper.insertNews(new News(null,message.getSentId(),message.getReciveId(),message.getContent(),new Date(),message.getExpire(),message.getInformType()));
        }
        return false;
    }

    @Override
    public boolean insertNews(News news) {
        return newsMapper.insertNews(news);
    }

    @Override
    public News[] getNewsByDate(Date date) {
        return newsMapper.getNewsByDate(date);
    }

    @Override
    public News[] getNewsByType(String informType) {
        return newsMapper.getNewsByType(informType);
    }
}
