package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MeetingRoomController {

    @Autowired
    MeetingRoomService meetingRoomService;

    @RequestMapping("/addMeetingRoom")
    public RetJson AddMeetingRoom(MeetingRoom meetingRoom){
        if(meetingRoomService.addMeetingRoom(meetingRoom)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }

    @RequestMapping("alterMeetingRoom")
    public RetJson alterMeetingRoom(MeetingRoom meetingRoom){
        if(meetingRoomService.alterMeetingRoom(meetingRoom)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }

    @RequestMapping("deleteMeetingRoom")
    public RetJson deleteMeetingRoom(Integer roomId){
        if(meetingRoomService.delteteMeetingRoom(roomId)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }

    @RequestMapping("/getMeetingRoom")
    public RetJson getMeetingRoomWithRoomId(Integer roomId){
        MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomWithRoomId(roomId);
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("meetingRoom",meetingRoom);
        return RetJson.succcess(map);
    }
}
