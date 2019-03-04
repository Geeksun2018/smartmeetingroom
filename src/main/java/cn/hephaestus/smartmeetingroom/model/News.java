package cn.hephaestus.smartmeetingroom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News {

    private Integer newsId;

    private Integer sentId;//发送者id

    private Integer reciveId;//接受者id

    public String content;//内容

    public Date time;//发送时间

    public long expire;//持续时间

    public String informType;//标识通知的类型

}
