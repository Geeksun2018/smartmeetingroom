package cn.hephaestus.smartmeetingroom.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    Integer articleId;

    String content;

    Integer userId;

    String articleName;

    Date creatTime;
    //1.2.3.4.5.6.11.12点赞的用户id 用'.'分割
    String likeList;
}
