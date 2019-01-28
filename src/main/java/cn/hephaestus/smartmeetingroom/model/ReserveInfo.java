package cn.hephaestus.smartmeetingroom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReserveInfo {
    //预定id
    private Integer reserveId;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")    //加上该注解之后，后端可以解析前端传过来的字符串
    @Future //必须是将来的日期
    private Date startTime;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private Date endTime;
    //房间id
    private Integer rid;
    //预定者
    private Integer uid;
    //参会者id
    private Integer[] participants;
    //会议标题
    private String meetingTopic;

    private Integer oid;//属于哪个组织的会议
    //会议状态
    private int flag;

}
