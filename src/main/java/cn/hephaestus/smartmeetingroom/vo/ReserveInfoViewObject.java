package cn.hephaestus.smartmeetingroom.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ReserveInfoViewObject {
    //会议室名称
    private String roomName;
    //会议主题
    private String topic;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //参会者id
    private Integer[] participants;
    //会议状态
    private int flag;
    //预定者
    private Integer reserveUid;
    //预定的部门
    private Integer reserveDid;
}
