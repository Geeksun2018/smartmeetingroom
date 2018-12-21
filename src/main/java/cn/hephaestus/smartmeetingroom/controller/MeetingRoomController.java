package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.MeetingRoomService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class MeetingRoomController {

    @Autowired
    MeetingRoomService meetingRoomService;
    @Autowired
    UserService userService;
    //1.添加会议室
    @RequestMapping("/addMeetingRoom")
    public RetJson addMeetingRoom(MeetingRoom meetingRoom, HttpServletRequest request){
        //获取当前用户
        User user=(User)request.getAttribute("user");
        //判断当前用户权限
        if (user.getRole()!=0){
            return RetJson.fail(-1,"无权限的操作");
        }

        if (!ValidatedUtil.validate(meetingRoom)){
            return RetJson.fail(-1,"请检查参数");
        }
        //设置oid
        meetingRoom.setOid(meetingRoom.getOid());
        if(meetingRoomService.addMeetingRoom(meetingRoom)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }
    //2.修改会议室信息
    @RequestMapping("alterMeetingRoom")
    public RetJson alterMeetingRoom(MeetingRoom meetingRoom,HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        if (user.getRole()!=0){
            return RetJson.fail(-1,"当前用户没有权限");
        }
        if (!ValidatedUtil.validate(meetingRoom)){
            return RetJson.fail(-1,"请检查参数");
        }
        meetingRoom.setOid(meetingRoom.getOid());
        if(meetingRoomService.alterMeetingRoom(meetingRoom)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }
    //3.删除会议室
    @RequestMapping("deleteMeetingRoom")
    public RetJson deleteMeetingRoom(Integer oid,Integer roomId,HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        if (user.getRole()!=0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        if(meetingRoomService.delteteMeetingRoom(oid,roomId)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"当前用户没有权限！");
    }
    //4.获取会议室
    @RequestMapping("/getMeetingRoom")
    public RetJson getMeetingRoomWithRoomId(Integer oid,Integer roomId,HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomWithRoomId(oid,roomId);
        if (meetingRoom==null){
            return RetJson.fail(-1,"找不到该会议室");
        }
        return RetJson.succcess("meetingRoom",meetingRoom);
    }

    //5.获取所有会议室
    @RequestMapping("/getMeetingRoomList")
    public RetJson getMeetingRoomList(Integer oid,HttpServletRequest request) {
        User user=(User)request.getAttribute("user");
        MeetingRoom[] meetingRooms=meetingRoomService.getMeetingRoomList(oid);
        return RetJson.succcess("meetingRooms",meetingRooms);
    }

    @RequestMapping("/reserveRoom")
    public RetJson reserveMeetingRoom(Integer roomId, Integer oid, Date beginTime,Integer duration,HttpServletRequest request){
        return RetJson.succcess(null);
    }
}
