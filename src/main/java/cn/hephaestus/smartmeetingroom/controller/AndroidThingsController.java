package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.*;
import cn.hephaestus.smartmeetingroom.service.*;
import com.arcsoft.face.FaceFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
public class AndroidThingsController {

    @Autowired
    MeetingRoomService meetingRoomService;
    @Autowired
    ReserveInfoService reserveInfoService;
    @Autowired
    UserService userService;
    @Autowired
    MeetingParticipantService meetingParticipantService;
    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    FaceInfoService faceInfoService;

    //获取当前所有的会议 有很大的安全隐患，有可能mac地址泄密!!!!!!!!
    @RequestMapping("/getAllMeeting")
    public RetJson getAllMeeting(String macAddress, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        System.out.println(date);
        //查找该mac地址对应的会议室id
        MeetingRoom meetingRoom=meetingRoomService.getMeetingRoomWithMacAddress(macAddress);

        //获取某一天该会议室所有的会议
        ReserveInfo[] reserveInfos=reserveInfoService.getReserveInfoByRoomId(meetingRoom.getRoomId(),date);

        //返回数据展示
        return RetJson.succcess("reserveInfos",reserveInfos);
    }

    //激活会议室
    @RequestMapping("/activationMeetingRoom")
    public RetJson activationMeetingRoom(MeetingRoom meetingRoom, HttpServletRequest request){
        //判断会议室是否已经激活成功
        User user= (User) request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        meetingRoom.setOid(userInfo.getOid());
        if (meetingRoomService.addMeetingRoom(meetingRoom)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"激活失败");
    }


    @RequestMapping("/faceOpen")
    public RetJson faceOpen(String encryptedString,String macAddress){
        //测试期间
        if (encryptedString!=null){
            return RetJson.succcess(null);
        }

        FaceFeature targetFaceFeature = new FaceFeature();
        FaceFeature sourceFaceFeature = new FaceFeature();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] featureData = decoder.decode(encryptedString);

        //设置目标人脸信息
        targetFaceFeature.setFeatureData(featureData);

        //根据mac地址查出该会议室
        MeetingRoom meetingRoom=meetingRoomService.getMeetingRoomWithMacAddress(macAddress);

        //获取当天该会议室所有的会议
        ReserveInfo[] reserveInfos=reserveInfoService.getReserveInfoByRoomId(meetingRoom.getRoomId(),new Date());

        ReserveInfo reserve=null;
        //找到下一个即将开始的一个会议
        for (ReserveInfo reserveInfo:reserveInfos){
            //该会议还没有结束且还未开启，或者正在进行
            if (reserveInfo.getEndTime().after(new Date())&&(reserveInfo.getFlag()==0||reserveInfo.getFlag()==1)){
                reserve=reserveInfo;
                break;
            }
        }
        if (reserve==null){
            return RetJson.fail(-1,"");
        }

        //找到参加该会的人
        Integer[] uids=meetingParticipantService.getParticipants(reserve.getReserveId());

        List<UserInfo> list=new LinkedList<>();
        UserInfo userInfo=new UserInfo();
        for (Integer uid:uids){
            userInfo=userService.getUserInfo(uid);
            list.add(userInfo);
        }

        List<UserFaceInfo> userFaceInfos=faceInfoService.getUserFaceInfoList(list);

        for (int i=0;i<userFaceInfos.size();i++){
            sourceFaceFeature.setFeatureData(userFaceInfos.get(i).getFeatureData());
            try {
                if (faceEngineService.compareFaceFeature(targetFaceFeature,sourceFaceFeature)>80){
                    //到达会议室
                    return RetJson.succcess(null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //对比一下人脸信息
        return RetJson.fail(-1,"身份错误");
    }

}
