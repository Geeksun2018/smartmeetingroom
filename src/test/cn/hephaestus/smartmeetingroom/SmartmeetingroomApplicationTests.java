package cn.hephaestus.smartmeetingroom;

import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartmeetingroomApplicationTests {

    @Autowired
    MeetingParticipantService meetingParticipantService;
    @Autowired
    RedisService redisService;
    @Test
    public void test(){
        //meetingParticipantService.addParticant(1,34);
        //meetingParticipantService.deleteParticant(1,34);
        //meetingParticipantService.deleteParticipants(3);
        //meetingParticipantService.addParticipants(3,new Integer[]{34,35,36});
        //redisService.sSet("Geeksun",new Integer[]{1,2,3});
        //System.out.println(redisService.sGet("Geeksun").toString());
    }



}