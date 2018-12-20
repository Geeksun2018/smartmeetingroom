package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReserveInfo {
    //预定id
    private Integer reserveId;
    //开始时间
    private Date startTime;
    //持续时长
    private Integer duration;
    //房间id
    private Integer rid;

}
