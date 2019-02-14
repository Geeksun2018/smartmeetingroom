package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.FileManagement;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.FileManagementService;
import cn.hephaestus.smartmeetingroom.service.MeetingParticipantService;
import cn.hephaestus.smartmeetingroom.service.ReserveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Validated
@RestController
public class FileManagementController {

    @Autowired
    FileManagementService fileManagementService;
    @Autowired
    ReserveInfoService reserveInfoService;
    @Autowired
    MeetingParticipantService meetingParticipantService;

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

    @RequestMapping("/uploadFle")
    public RetJson saveFile(FileManagement fileManagement,@RequestParam("file") MultipartFile multipartFile){
        String username = userInfo.getName();
        String url = fileManagementService.uploadFileAndGetUrl(multipartFile,"groupFile/" + username + UUID.randomUUID() + "_" +  fileManagement.getFileName());
        if(url == null){
            return RetJson.fail(-1,"上传文件失败！");
        }
        fileManagement.setOid(oid);
        fileManagement.setDid(did);
        fileManagement.setUid(user.getId());
        if(reserveInfoService.getReserveInfoByReserveId(oid,fileManagement.getMid()) == null){
            return RetJson.fail(-2,"参数错误");
        }
        //检查此人是否在会议中
        Integer[] participants = meetingParticipantService.getParticipants(oid,fileManagement.getMid());
        Integer flag = 1;
        for(Integer participant:participants){
            if(participant == user.getId()){
                flag = 0;
                break;
            }
        }
        if(flag == 1){
            return RetJson.fail(-3,"非法操作！");
        }

        fileManagement.setPath(url);
        fileManagementService.saveFile(fileManagement);
        return RetJson.succcess("fileId",fileManagement.getId());
    }

    @RequestMapping("/getFile")
    public RetJson getFile(Integer id,Integer mid){
        FileManagement fileManagement = fileManagementService.getFile(id);
        if(fileManagement == null){
            return RetJson.fail(-2,"请检查参数！");
        }
        if(mid != fileManagement.getMid()){
            return RetJson.fail(-1,"非法操作!");
        }
        return RetJson.succcess("file",fileManagement);
    }

    @RequestMapping("/getFiles")
    public RetJson getMeetingFiles(Integer mid){
        Integer[] participants = meetingParticipantService.getParticipants(oid,mid);
        Integer flag = 0;
        for(Integer participant:participants){
            if(participant.equals(user.getId())){
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            return RetJson.fail(-3,"非法操作！");
        }
        FileManagement[] fileManagements = fileManagementService.getMeetingFiles(mid);
        return RetJson.succcess("fileManagements",fileManagements);
    }

    @RequestMapping("/delFile")
    public RetJson deleteFile(Integer fileId){
        FileManagement fileManagement = fileManagementService.getFile(fileId);
        if(fileManagement == null){
            return RetJson.fail(-1,"参数错误！");
        }
        if(user.getRole() == 0&&fileManagement.getUid()!=user.getId()){
            return RetJson.fail(-2,"非法操作！");
        }
        fileManagementService.deleteFile(fileId);
        return RetJson.succcess(null);
    }
}
