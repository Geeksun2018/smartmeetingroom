package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MeetingRoom {
    private int roomId;
    private int oid;//组织id
    private String roomName;
    private int capacity;
    private String address;
}
