package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String macAddress;//mac地址,使用md5加密
}
