package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class AgentEvent {
    private int id;//id
    private int uid;//用户id

    @NotNull
    @Length(min = 0,max = 100)
    private String content;//内容

    private boolean state=false;//完成状态

    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//开始时间

    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//截止时间

    private Date createTime;

}
