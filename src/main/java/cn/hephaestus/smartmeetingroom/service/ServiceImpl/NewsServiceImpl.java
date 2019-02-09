package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.service.NewsService;
import cn.hephaestus.smartmeetingroom.websocket.MessageSocketServer;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {


    @Override
    public boolean sendNews(String news, Integer[] idArr) {
        MessageSocketServer.sentAll(idArr,news);
        return true;
    }
}
