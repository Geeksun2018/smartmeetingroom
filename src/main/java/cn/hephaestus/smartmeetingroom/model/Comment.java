package cn.hephaestus.smartmeetingroom.model;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    private Integer commentId;

    private Integer articleId;

    private String content;

    private Integer uid;

    private Date creatTime;
}
