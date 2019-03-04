package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Message;
import cn.hephaestus.smartmeetingroom.model.News;

import java.util.Date;

public interface NewsService {

    public boolean sendNews(String news,Integer[] idArr,String informType);

    public boolean sendNewsToObject(Message message,Integer[] idArr);

    public boolean insertNews(News news);

    public News[] getNewsByDate(Date date);

    public News[] getNewsByType(String informType);
}
