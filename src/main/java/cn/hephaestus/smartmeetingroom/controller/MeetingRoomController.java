package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.MeetingRoom;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.*;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会议室的管理
 */
@RestController
@Validated
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
    User user=null;
    UserInfo userInfo=null;


    @ModelAttribute
    public void comment(HttpServletRequest request){
        if (user!=null&&userInfo!=null){
            return;
        }
        user=(User)request.getAttribute("user");
        userInfo = (UserInfo)request.getAttribute("userInfo");
    }
    //1.添加会议室
    @RequestMapping("/addMeetingRoom")
    public RetJson addMeetingRoom(MeetingRoom meetingRoom){
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
    public RetJson alterMeetingRoom(MeetingRoom meetingRoom){
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
    @RequestMapping("/deleteMeetingRoom")
    public RetJson deleteMeetingRoom(Integer oid,Integer roomId){
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
    public RetJson getMeetingRoomWithRoomId(Integer roomId){
        MeetingRoom meetingRoom = meetingRoomService.getMeetingRoomWithRoomId(roomId);
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
    public RetJson reserveMeetingRoom(@Valid ReserveInfo reserveInfo){
        Integer oid = userInfo.getOid();
        if(user.getId() != reserveInfo.getReserveUid()){
            return RetJson.fail(-1,"操作非法！");
        }
        //判断该用户是否拥有预定会议室的权限
        if (user.getRole()==0){
            return RetJson.fail(-1,"你没有预定会议室的权限");
        }
        //判断参会人员是否合法,存在且有空闲时间
        Set<Integer> set=meetingRoomService.getAllConficUser(oid,reserveInfo.getStartTime(),reserveInfo.getEndTime());
        for(Integer participant:reserveInfo.getParticipants()){
            if(userService.getUserByUserId(participant) == null){
                return RetJson.fail(-1,"参与者暂未注册！");
            }
            if (set.contains(participant)){
                return RetJson.fail(-1,"用户"+participant+"忙碌");
            }
        }

        //看该会议室是否存在
        MeetingRoom room=meetingRoomService.getMeetingRoomWithRoomId(reserveInfo.getRid());

        //预定会议室
        if(room!=null){
            //与该时间段有交集的reserveInfo
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReserveInfo[] reserveInfos = reserveInfoService.queryIsAvailable(reserveInfo.getRid(),sdf.format(reserveInfo.getStartTime()),sdf.format(reserveInfo.getEndTime()));
            if(reserveInfos.length == 0){
                //会议室有效
                System.out.println(reserveInfo.getStartTime().toString());
                reserveInfoService.addReserveInfo(reserveInfo);
                //插入后直接映射到实体类了!!!
                Integer reserveInfoId = reserveInfo.getReserveId();
                meetingParticipantService.addParticipants(oid,reserveInfoId,reserveInfo.getParticipants());
                //预定成功，发送消息给所有参会用户
                return RetJson.succcess("meetingId",reserveInfoId);
            }
            return RetJson.fail(-1,"会议室已被占用！");
        }
        return RetJson.fail(-1,"会议室不存在！");
    }

    @RequestMapping("/roomIsAvailable")
    public RetJson checkRoomIsAvailable(Integer roomId, Integer oid, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future(message = "时间必须在当前时间之前")
            Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future(message = "时间必须在当前时间之前") Date endTime, HttpServletRequest request){
        if(!meetingRoomService.getMeetingRoomWithRoomId(roomId).isAvailable()){
            return RetJson.fail(-1,"会议室暂时不能使用，请询问管理员！");
        }
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = userService.getUserInfo(user.getId());
        if(userInfo.getOid() == oid){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReserveInfo[] reserveInfos = reserveInfoService.queryIsAvailable(roomId,sdf.format(startTime),sdf.format(endTime));
            if(reserveInfos.length == 0){
                return RetJson.succcess(null);
            }else {
                return RetJson.fail(-1,"已被预订！");
            }
        }
        return RetJson.fail(-1,"只能查询自己公司的会议室");
    }

    @RequestMapping("/checkAllRooms")
    public RetJson checkAllOrganizationMeetingRooms(Integer oid,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future(message = "时间必须在当前时间之前")
            Date startTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future(message = "时间必须在当前时间之前") Date endTime){
        if(oid != userInfo.getOid()){
            return RetJson.fail(-1,"非法操作！");
        }
        Set<String> keys = redisService.sGetByPattern(oid + "cm");
        Set<Integer> roomSet = new LinkedHashSet<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDate = sdf.format(startTime);
        String endDate = sdf.format(endTime);
        for(String key:keys){
            key = key.substring(key.indexOf('m') + 1);
            Integer reserveId = Integer.parseInt(key);
            Integer occupiedRoomId = reserveInfoService.queryIsAvailableByReserveId(reserveId,beginDate,endDate);
            if(occupiedRoomId != null){
                roomSet.add(occupiedRoomId);
            }
        }
        return RetJson.succcess("occupiedRoomId",roomSet);
    }

    @RequestMapping("/cancelReservation")
    public RetJson cancelReservationByMid(Integer mid){
        if(user.getRole() == 0){
            RetJson.fail(-1,"您的权限不够，取消失败！");
        }
        if(reserveInfoService.deleteReserveInfo(userInfo.getOid(),mid)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"取消失败！");
    }

    @RequestMapping("/updateReservation")
    public RetJson updateReservation(@Valid ReserveInfo reserveInfo){
        if(user.getId() != reserveInfo.getReserveUid()){
            return RetJson.fail(-1,"操作非法！");
        }
        if(user.getRole() == 0){
            RetJson.fail(-1,"您的权限不够，预定失败！");
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(reserveInfoService.queryIsAvailable(reserveInfo.getRid(),sdf.format(reserveInfo.getStartTime()),sdf.format(reserveInfo.getEndTime())).length == 0){
            reserveInfoService.updateReserveInfo(reserveInfo);
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"操作失败!");
    }
}
