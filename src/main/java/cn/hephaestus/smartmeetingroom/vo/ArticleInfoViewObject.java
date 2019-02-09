package cn.hephaestus.smartmeetingroom.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleInfoViewObject {
    private Integer id;
    //内容
    private String content;

    //发布时间
    private Date createTime;

    //发布者id
    private Integer uid;

    //评论数
    private Integer commentCount;

    //阅读数
    private Integer readCount;

    //点赞数
    private Integer likeCound;

    //用户是否点赞了
    private boolean isLike;
}
