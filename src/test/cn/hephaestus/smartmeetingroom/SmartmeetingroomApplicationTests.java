package cn.hephaestus.smartmeetingroom;

//import cn.hephaestus.smartmeetingroom.mapper.UserFaceInfoMapper;

import cn.hephaestus.smartmeetingroom.mapper.OrganizationMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserFaceInfoMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserInfoMapper;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//import cn.hephaestus.smartmeetingroom.model.UserFaceInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartmeetingroomApplicationTests {

    @Autowired
    MeetingRoomService meetingRoomService;
    @Autowired
    UserService userService;
    @Autowired
    UserFaceInfoMapper userFaceInfoMapper;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    OrganizationMapper organizationMapper;
    @Autowired
    FaceInfoService faceInfoService;
    @Autowired
    ReserveInfoService reserveInfoService;


    @Test
    public void testGetUserInfo() {
        System.out.println(userService.getUserInfo(1));
    }

    @Test
    public void testAlterUserInfo(){
//        UserInfo userInfo = new UserInfo();
//        userInfo.setId(34);
//        userInfo.setPhoneNum("18390254596");
//        userInfo.setEmail("18100738792@163.com");
//        userInfo.setImagePath("https://smartmeetingroom-1257009269.cos.ap-guangzhou.myqcloud.com/head_portrait/18100738792_headportrait");
//        userInfo.setSex(true);
//        userInfo.setNickName("Zeng");
//        userInfo.setName("曾宪辉");
//        userService.alterUserInfo(34,userInfo);
        System.out.println(userInfoMapper.getUserInfoById(34));
    }

    @Test
    public void testRegister(){
//        User user = new User();
//        user.setUsername("18934698676");
//        user.setPassword("e50d7766fed67889634e07892e6954cd");
//        user.setSalt("OtMMGz7j");
//        userService.register(user);
    }

    @Test
    public void addMeetingRoom(){
//        MeetingRoom mettingRoom = new MeetingRoom();
//        mettingRoom.setRoomName("Geeksun");
//        mettingRoom.setCapacity(40);
//        meetingRoomService.addMeetingRoom(mettingRoom);
       // System.out.println(meetingRoomService.getMeetingRoomWithRoomId(1));
//        System.out.println(meetingRoomService.getMeetingRoomWithRoomName("Geeksun"));
//        meetingRoomService.delteteMeetingRoom(1);
    }

    @Test
    public void addUserFaceInfo(){
//        UserFaceInfo userFaceInfo = new UserFaceInfo();
//        userFaceInfo.setFeatureData("123456789");
//        System.out.println(userFaceInfoMapper.addFaceInfo(userFaceInfo));
//        System.out.println(userFaceInfoMapper.getFaceInfo(1));
//        userFaceInfo.setFaceInfoId(1);
//        userFaceInfo.setFeatureData("8746321");
//        userFaceInfoMapper.updateFaceInfo(userFaceInfo);
       // userFaceInfoMapper.deleteFaceInfo(1);
    }

    @Test
    public void departmentTest(){
//        Department department = new Department();
//        department.setDepartmentName("服务端技术部");
//        department.setOid(5);
//        department.setDepartmentId(1);
//        departmentService.addDepartment(department);
//        departmentService.alterDepartment(department);
//        departmentService.deleteDepartment(3,5);
        //System.out.println(departmentService.getDepartment(1));
//        System.out.println(departmentService.getDepartmentList(5)[0]);
//        System.out.println(departmentService.getDepartmentList(5)[1]);
//        SimpleHash simpleHash=new SimpleHash("md5","123456",byteSource,ENCRYPT_NUM);
//        user.setPassword(simpleHash.toHex());
 //       System.out.println(organizationMapper.getOrganization(5));
          Department department = new Department();
          department.setDepartmentName("测试部");
          department.setOid(5);
            System.out.println(departmentService.addDepartment(department));
          System.out.println(department.getDepartmentId());
    }

    @Test
    public void userFaceInfoTest(){
//        byte[] bytes = new byte[]{1,2,3,4,5};
//        UserFaceInfo userFaceInfo = new UserFaceInfo();
//        userFaceInfo.setFeatureData(bytes);
//        faceInfoService.addFaceInfo(userFaceInfo);
//        System.out.println(userFaceInfo.getFaceInfoId());
//        if(userService.setFid(userFaceInfo.getFaceInfoId(),34)){
//            System.out.println("1");
//        }
    }

    @Test
    public void reserveInfoTest(){
//        ReserveInfo reserveInfo = reserveInfoService.getReserveInfoByReserveId(1);
//        ReserveInfo reserveInfo = new ReserveInfo();
//        reserveInfo.setRid(5);
//        reserveInfo.setDuration(135);
//        reserveInfo.setStartTime(new Date());
        //reserveInfoService.addReserveInfo(reserveInfo);
//        System.out.println(reserveInfoService.getReserveInfoByReserveId(1));
//        System.out.println(reserveInfoService.getReserveInfoByRoomId(5));
//        reserveInfoService.updateReserveInfo(reserveInfo);
        reserveInfoService.deleteReserveInfo(2);
    }

}