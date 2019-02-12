package cn.hephaestus.smartmeetingroom.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 定义一条消息
 */
@Setter
@Getter
@AllArgsConstructor
public class Message{
    public Message() {
    }
    public static final String MEETING="meeting";
    public static final String DEPARTMENT="department";
    public static final String PERSON="person";
    public static final String NEWS="news";
    private String type;//种类

    private Integer sentId;//发送者id

    private Integer reciveId;//接受者id

    public String content;//内容

    public long time;//发送时间

    public long expire;//持续时间

    public String informType;//标识通知的类型

    public String toString(){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{}";
    }
}

