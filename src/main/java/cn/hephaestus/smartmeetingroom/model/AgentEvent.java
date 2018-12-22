package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AgentEvent {
    private int id;//id
    private int uid;//用户id
    private String content;//内容
    private boolean status=false;//完成状态
    private Date data;//添加时间
    private Date startTime;//开始时间
    private Date endTime;//截止时间
    private Date createTime;
}
