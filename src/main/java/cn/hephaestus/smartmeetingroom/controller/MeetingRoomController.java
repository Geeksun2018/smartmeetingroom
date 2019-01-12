package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.*;
import cn.hephaestus.smartmeetingroom.service.*;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import com.arcsoft.face.FaceFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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
    public RetJson reserveMeetingRoom(Integer roomId, Integer oid, String beginTime,String endTime,HttpServletRequest request){
        Date beginDate;
        Date endDate;
        ReserveInfo reserveInfo = new ReserveInfo();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = userService.getUserInfo(34);
        System.out.println(beginTime);
        try {
            beginDate = format.parse(beginTime);
            endDate = format.parse(endTime);
        }catch (Exception e){
            e.printStackTrace();
            return RetJson.fail(-1,"时间格式错误！");
        }
        //只能预定自己公司的房间
        if(userInfo.getOid() == oid){
            if(reserveInfoService.queryIsAvaliable(roomId,beginTime,endTime).length == 0){
                reserveInfo.setStartTime(beginDate);
                reserveInfo.setEndTime(endDate);
                reserveInfo.setRid(roomId);
                reserveInfoService.addReserveInfo(reserveInfo);
                return RetJson.succcess(null);
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

    @RequestMapping("/judgeFeatureData")
    public RetJson uploadFeatureData(String encryptedString,Integer oid,Integer roomId,HttpServletRequest request){
        FaceFeature targetFaceFeature = new FaceFeature();
        FaceFeature sourceFaceFeature = new FaceFeature();
        User user = (User)request.getAttribute("user");
        Base64.Decoder decoder = Base64.getDecoder();
        //通过解密的算法，将encryptedString解密成bytes
        byte[] featureData = decoder.decode(encryptedString);
        targetFaceFeature.setFeatureData(featureData);
        //暂时先查询是否为该公司的员工
        //1.先查询将在该会议室开会的用户
        List<UserFaceInfo> lists = new LinkedList<>();
        lists = faceInfoService.getUserFaceInfoList(userService.getUserinfoListByOid(oid));

        //3.将传入的人脸信息与list中的比较
        for(int i = 0;i < lists.size();i++){
            sourceFaceFeature.setFeatureData(lists.get(i).getFeatureData());
            try {
                if(faceEngineService.compareFaceFeature(targetFaceFeature,sourceFaceFeature) > 80){
                    return RetJson.succcess(null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return RetJson.fail(-1,"人脸验证失败！");
    }
}
