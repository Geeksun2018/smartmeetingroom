package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.*;
import cn.hephaestus.smartmeetingroom.service.*;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 会议室的管理
 */
@RestController
public class MeetingRoomController {

    @Autowired
    MeetingRoomService meetingRoomService;
    @Autowired
    UserService userService;
    @Autowired
    ReserveInfoService reserveInfoService;
    @Autowired
    FaceInfoService faceInfoService;
    @Autowired
    FaceEngineService faceEngineService;
    @Autowired
    MeetingParticipantService meetingParticipantService;
    @Autowired
    RedisService redisService;
    //1.添加会议室
    @RequestMapping("/addMeetingRoom")
    public RetJson addMeetingRoom(MeetingRoom meetingRoom, HttpServletRequest request){
        //获取当前用户
        User user=(User)request.getAttribute("user");
        //判断当前用户权限
        if (user.getRole()==0){
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
        if (user.getRole()==0){
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
        if (user.getRole()==0){
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
    public RetJson reserveMeetingRoom(ReserveInfo reserveInfo,HttpServletRequest request){
        System.out.println(reserveInfo);
        User user=(User) request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        //判断该用户是否拥有预定会议室的权限
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有预定会议室的权限");
        }

        //判断参会人员是否合法
        for(Integer participant:reserveInfo.getParticipants()){
            if(userService.getUserByUserId(participant) == null){
                return RetJson.fail(-1,"参与者暂未注册！");
            }
        }

        //看该会议室是否存在
        MeetingRoom room=meetingRoomService.getMeetingRoomWithRoomId(userInfo.getOid(),reserveInfo.getRid());

        //预定会议室
        if(room!=null){
            if(reserveInfoService.queryIsAvaliable(reserveInfo.getRid(),reserveInfo.getStartTime().toString(),reserveInfo.getEndTime().toString()).length == 0){
                Integer reserveInfoId = reserveInfoService.addReserveInfo(reserveInfo);
                meetingParticipantService.addParticipants(reserveInfoId,reserveInfo.getParticipants());
                redisService.sadd("cm" + reserveInfoId,toStringArray(reserveInfo.getParticipants()));
                redisService.expire("cm" + reserveInfoId,31536000);
                return RetJson.succcess("meetingId",reserveInfoId);
            }
            return RetJson.fail(-1,"会议室已被占用！");
        }
        return RetJson.fail(-1,"只能预定自己公司的会议室！");
    }
    @RequestMapping("/roomIsAvailable")
    public RetJson checkRoomIsAvailable(Integer roomId, Integer oid, String beginTime,String endTime,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = userService.getUserInfo(user.getId());
        if(userInfo.getOid() == oid){
            ReserveInfo[] reserveInfos = reserveInfoService.queryIsAvaliable(roomId,beginTime,endTime);
            Map map = new LinkedHashMap();
            map.put("reserveInfos",reserveInfos);
            return RetJson.succcess(map);
        }
        return RetJson.fail(-1,"只能查询自己公司的会议室");
    }

    @RequestMapping("/updateParticipants")
    public RetJson updateMeetingParticipants(Integer reserveId,Integer[] participants){
        if(reserveInfoService.getReserveInfoByReserveId(reserveId) == null){
            return RetJson.fail(-1,"未查询到预定信息！");
        }
        for(Integer participant:participants){
            if(userService.getUserByUserId(participant) == null){
                return RetJson.fail(-1,"参与者暂未注册！");
            }
        }
        meetingParticipantService.deleteParticipants(reserveId);
        redisService.del("cm" + reserveId);
        meetingParticipantService.addParticipants(reserveId,participants);
        redisService.sadd("cm" + reserveId,toStringArray(participants));
        return RetJson.succcess(null);
    }

    @RequestMapping("/deleteParticipant")
    public RetJson deleteMeetingParticipant(Integer reserveId,Integer participant){
        if(userService.getUserByUserId(participant) == null){
            return RetJson.fail(-1,"参与者暂未注册！");
        }
        meetingParticipantService.deleteParticant(reserveId,participant);
        redisService.sdel("cm" + reserveId,participant.toString());
        return RetJson.succcess(null);
    }

    @RequestMapping("/addParticipant")
    public RetJson addMeetingParticipant(Integer reserveId,Integer participant){
        if(reserveInfoService.getReserveInfoByReserveId(reserveId) == null){
            return RetJson.fail(-1,"预定信息不存在！");
        }
        if(userService.getUserByUserId(participant) == null){
            return RetJson.fail(-1,"参与者暂未注册！");
        }
        meetingParticipantService.addParticant(reserveId,participant);
        Set set = redisService.sget("cm" + reserveId);
        set.add(participant.toString());
        redisService.sadd("cm" + reserveId,toStringArray(set));
        return RetJson.succcess(null);
    }

    @RequestMapping("/getParticipants")
    public RetJson addMeetingParticipant(Integer reserveId){
        Set set = redisService.sget("cm" + reserveId);
        return RetJson.succcess("participants",set);
    }


    String[] toStringArray(Integer[] arrays){
        List<String> lString = new ArrayList<>();
        for(Integer num:arrays){
            lString.add(num.toString());
        }
        return (String[])lString.toArray();
    }

    String[] toStringArray(Set set){
        List<String> lString = new ArrayList<>();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String str = it.next();
            lString.add(str);
        }
        return (String[])lString.toArray();
    }
}
