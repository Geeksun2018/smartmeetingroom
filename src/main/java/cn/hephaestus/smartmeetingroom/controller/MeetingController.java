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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
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

    User user=null;
    UserInfo userInfo=null;
    Integer oid = null;
    Integer did = null;
    @ModelAttribute
    public void comment(HttpServletRequest request){
        user = (User)request.getAttribute("user");
        userInfo = (UserInfo)request.getAttribute("userInfo");
        oid = userInfo.getOid();
        did = userInfo.getDid();
    }

    @RequestMapping("/updateParticipants")
    public RetJson updateMeetingParticipants(Integer reserveId, Integer[] participants){
        Integer oid = userInfo.getOid();
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有预定会议室的权限");
        }
        if(reserveInfoService.getReserveInfoByReserveId(oid,reserveId) == null){
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
    public RetJson deleteMeetingParticipant(Integer reserveId,Integer participant){
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
    public RetJson addMeetingParticipant(Integer reserveId,Integer participant){
        Integer oid = userInfo.getOid();
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有增加参与者的的权限");
        }
        if(reserveInfoService.getReserveInfoByReserveId(oid,reserveId) == null){
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
        Integer oid = userInfo.getOid();
        Set set = redisService.sget(oid + "cm" + reserveId);
        return RetJson.succcess("participants",set);
    }

    @RequestMapping("/getMeetingInfo")
    public RetJson getMeetingInfoByMid(Integer mid){
        Integer oid = userInfo.getOid();
        ReserveInfo reserveInfo = reserveInfoService.getReserveInfoByReserveId(oid,mid);
        return RetJson.succcess("reserveInfo",reserveInfo);
    }


    @RequestMapping("/getMeetingInfoByCondition")
    public RetJson getMeetingInfoByCondition(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date, Integer rid, Integer did, Integer oid){
        List<ReserveInfoViewObject> list = reserveInfoService.getReserveInfoViewObjectByCondition(date,rid,did,userInfo.getOid());
        return RetJson.succcess("list",list);
    }

    @RequestMapping("/getMeetingCount")
    public RetJson getCountOfDepartmentMeeting(Integer type,Integer year,Integer month){
        if(type == null){
            return RetJson.fail(-1,"参数错误！");
        }
        Integer count = 0;
        //0表示按年查询
        if(type == 0){
            count = reserveInfoService.queryCountOfDepartmentMeetingByYear(oid,did,year);
        }else{
            count = reserveInfoService.queryCountOfDepartmentMeetingByMonth(oid,did,year,month);
        }
        return RetJson.succcess("count",count);
    }

    @RequestMapping("/getMeetingTimeCount")
    public RetJson getCountOfDepartmentMeetingTime(Integer type,Integer year,Integer month){
        if(type == null){
            return RetJson.fail(-1,"参数错误！");
        }
        Integer count = 0;
        //0表示按年查询
        if(type == 0){
            count = reserveInfoService.queryCountOfDepartmentMeetingTimeByYear(oid,did,year);
        }else{
            count = reserveInfoService.queryCountOfDepartmentMeetingTimeByMonth(oid,did,year,month);
        }
        return RetJson.succcess("count",count);
    }

    @RequestMapping("/getMeetingCountByDay")
    public RetJson getCountOfMeeting(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        Map<Date,Integer> map = null;
        map = reserveInfoService.queryCountOfMeetingByDay(date);
        return RetJson.succcess("count",map);
    }
}
