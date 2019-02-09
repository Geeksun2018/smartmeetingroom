package cn.hephaestus.smartmeetingroom.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    Integer articleId;

    @Length(max = 300,min = 1)
    @NotNull
    String content;

    Integer userId;

    Integer oid;

    Date creatTime;
    //1.2.3.4.5.6.11.12点赞的用户id 用'.'分割
    String likeList;

    Integer commentCount;

    Integer readCount;
}
