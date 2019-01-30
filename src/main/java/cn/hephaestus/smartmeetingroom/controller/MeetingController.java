package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.*;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
public class MeetingController {

    @Autowired
    UserService userService;
    @Autowired
    ReserveInfoService reserveInfoService;
    @Autowired
    MeetingParticipantService meetingParticipantService;
    @Autowired
    RedisService redisService;
    @Autowired
    MeetingRoomService meetingRoomService;

    @RequestMapping("/updateParticipants")
    public RetJson updateMeetingParticipants(Integer reserveId, Integer[] participants, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        Integer oid = userInfo.getOid();
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有预定会议室的权限");
        }
        if(reserveInfoService.getReserveInfoByReserveId(reserveId) == null){
            return RetJson.fail(-1,"未查询到预定信息！");
        }
        for(Integer participant:participants){
            if(userService.getUserByUserId(participant) == null){
                return RetJson.fail(-1,"参与者暂未注册！");
            }
        }
        meetingParticipantService.deleteParticipants(oid,reserveId);
        meetingParticipantService.addParticipants(oid,reserveId,participants);
        return RetJson.succcess(null);
    }

    @RequestMapping("/deleteParticipant")
    public RetJson deleteMeetingParticipant(Integer reserveId,Integer participant,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        Integer oid = userInfo.getOid();
        if(userService.getUserByUserId(participant) == null){
            return RetJson.fail(-1,"参与者暂未注册！");
        }
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有删除参与者的的权限");
        }
        if(meetingParticipantService.deleteParticipant(oid,reserveId,participant)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"删除失败");
    }

    @RequestMapping("/addParticipant")
    public RetJson addMeetingParticipant(Integer reserveId,Integer participant, HttpServletRequest request){
        User user=(User) request.getAttribute("user");
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        Integer oid = userInfo.getOid();
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有增加参与者的的权限");
        }
        if(reserveInfoService.getReserveInfoByReserveId(reserveId) == null){
            return RetJson.fail(-1,"预定信息不存在！");
        }
        if(userService.getUserByUserId(participant) == null){
            return RetJson.fail(-1,"参与者暂未注册！");
        }
        meetingParticipantService.addParticipant(oid,reserveId,participant);
        return RetJson.succcess(null);
    }

    @RequestMapping("/getParticipants")
    public RetJson addMeetingParticipant(Integer reserveId, HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        Integer oid = userInfo.getOid();
        Set set = redisService.sget(oid + "cm" + reserveId);
        return RetJson.succcess("participants",set);
    }

    @RequestMapping("/getMeetingInfo")
    public RetJson getMeetingInfoByMid(Integer mid){
        ReserveInfo reserveInfo = reserveInfoService.getReserveInfoByReserveId(mid);
        return RetJson.succcess("reserveInfo",reserveInfo);
    }

    @RequestMapping("/getMeetingInfoByCondition")
    public RetJson getMeetingInfoByCondition(@Validated @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date, Integer rid, Integer did, Integer oid, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        if(userInfo.getOid() != oid){
            RetJson.fail(-1,"非法操作！");
        }
        List<ReserveInfoViewObject> list = reserveInfoService.getReserveInfoViewObjectByCondition(date,rid,did);
        return RetJson.succcess("list",list);
    }
}