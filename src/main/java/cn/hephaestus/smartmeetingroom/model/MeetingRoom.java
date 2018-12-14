package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class MeetingRoom {
    private int roomId;
    private int oid;//组织id
    @Length(min = 1,max = 30)
    private String roomName;
    private int capacity;
    @Length(min = 1,max = 30)
    private String address;
}
