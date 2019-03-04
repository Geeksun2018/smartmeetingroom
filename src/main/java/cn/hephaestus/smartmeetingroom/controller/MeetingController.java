package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.*;
import cn.hephaestus.smartmeetingroom.model.ReserveInfo;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.*;
import cn.hephaestus.smartmeetingroom.vo.ReserveInfoViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    NewsService newsService;
    @Autowired
    OrganizationService organizationService;

    User user=null;
    UserInfo userInfo=null;
    @ModelAttribute
    public void comment(HttpServletRequest request){
        user = (User)request.getAttribute("user");
        userInfo = (UserInfo)request.getAttribute("userInfo");
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
        meetingParticipantService.addParticipants(user.getId(),oid,reserveId,participants);
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

    //点到功能
    @RequestMapping("/checkInParticipant")
    public RetJson checkInParticipant(Integer mid){
        Integer oid = userInfo.getOid();
        //获取参会人员列表
        Set set1 = redisService.sget(oid + "cm" + mid);
        Integer size1=set1.size();//应该参会人数
        //获取签到人员列表
        Set set2=redisService.sget(oid+"sm"+mid);
        Integer size2=set2.size();//已经签到人数

        Set set3=redisService.sget(oid+"lm"+mid);
        Integer size3=0;
        if (set3!=null){
            size3=set3.size();
        }

        Map<String,String> res=new HashMap<>();
        res.put("all",size1.toString());
        res.put("checkIn",size2.toString());
        res.put("askForLeave",size3.toString());
        return RetJson.succcess("situation",res);
    }

    //请假功能
    @RequestMapping("/askForLeave")
    private RetJson askForLeave(Integer mid,String reason){
        ReserveInfo reserveInfo=reserveInfoService.getReserveInfoByReserveId(userInfo.getOid(),mid);
        Integer reserveUid=reserveInfo.getReserveUid();//获取召开者
        Integer[] arr=new Integer[1];
        arr[0]=reserveUid;
        newsService.sendNews("用户"+user.getUsername()+"希望请假，理由是"+reason,arr,"other");
        redisService.sadd(reserveInfo.getReserveOid()+"lm"+mid,user.getId().toString());
        return RetJson.succcess(null);
    }

    //推迟时间功能
    @RequestMapping("/postponeTheMeeting")
    public RetJson postponeTheMeeting(Integer mid){
        ReserveInfo reserveInfo=reserveInfoService.getReserveInfoByReserveId(userInfo.getId(),userInfo.getOid(),mid);
        //分别试探推迟十分钟，二十分钟，三十分钟是否可行
        Date date1=reserveInfo.getStartTime();
        Date date2=reserveInfo.getEndTime();

        ReserveInfo[] arr=null;
        Set<ReserveInfo> temp=new HashSet<>();

        long delay=0;
        for (int i=3;i>=1;i--){
            delay=1000*60*10*i;
            arr=reserveInfoService.queryIsAvailable(reserveInfo.getRid(),new Date(date1.getTime()+delay),new Date(date2.getTime()+delay));
            temp.clear();
            for (ReserveInfo r:arr){
                if (r.getReserveId()!=mid){
                    temp.add(r);
                }
            }

            if (temp.size()==0){
                reserveInfo.setStartTime(new Date(date1.getTime()+delay));
                reserveInfo.setEndTime(new Date(date2.getTime()+delay));
                if (reserveInfoService.updateReserveInfo(reserveInfo)){
                    Set<String> set = redisService.sget(userInfo.getOid() + "cm" + mid);
                    pushNews("你的会议["+reserveInfo.getTopic()+"]推迟了"+delay/(1000*60),set);
                    return RetJson.succcess("delay",delay/(1000*60));
                }
            }
        }

        return RetJson.fail(-1,"无法为你推迟时间");
    }

    //催促用户尽快到场
    @RequestMapping("/urgeParticipant")
    public RetJson UrgeParticipant(Integer mid){
        Integer oid = userInfo.getOid();
        //获取参会人员列表
        Set<String> set1 = redisService.sget(oid + "cm" + mid);
        //获取签到人员列表
        Set<String> set2=redisService.sget(oid+"sm"+mid);
        set1.removeAll(set2);
        pushNews("你有一个会议，召开者提醒你抓紧时间入场",set1);
        return RetJson.succcess(null);
    }
    private void pushNews(String news,Set<String> set){
        List list=new ArrayList();
        for (String s:set){
            list.add(Integer.parseInt(s));
        }
        Integer[] arr=new Integer[list.size()];
        list.toArray(arr);
        newsService.sendNews(news,arr,"other");//other表示给指定用户集合发通知
    }

    @RequestMapping("/getMeetingCount")
    public RetJson getCountOfDepartmentMeeting(Integer type,Integer year,Integer month){
        if(type == null){
            return RetJson.fail(-1,"参数错误！");
        }
        Integer count = 0;
        //0表示按年查询
        if(type == 0){
            count = reserveInfoService.queryCountOfDepartmentMeetingByYear(userInfo.getOid(),userInfo.getDid(),year);
        }else{
            count = reserveInfoService.queryCountOfDepartmentMeetingByMonth(userInfo.getOid(),userInfo.getDid(),year,month);
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
            count = reserveInfoService.queryCountOfDepartmentMeetingTimeByYear(userInfo.getOid(),userInfo.getDid(),year);
        }else{
            count = reserveInfoService.queryCountOfDepartmentMeetingTimeByMonth(userInfo.getOid(),userInfo.getDid(),year,month);
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
