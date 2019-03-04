package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.News;

public interface NewsService {

    public boolean sendNews(String news,Integer[] idArr);

    public boolean insertNews(News news);

}
