package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
//@NoArgsConstructor
public class MeetingRoom {

    private int roomId;

    private int oid;//组织id

    private String roomName;

    private int capacity;

    private String address;
    //mac地址,使用md5加密
    private String macAddress;
    //true表示可用，false表示不可用
    private boolean available;
    //设备注册时的时间
    private Timestamp registerTime;
}
