package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
@RestController
public class DepartMentController {

    private final static int MAX_SIZE=1024*1024*5;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    UserService userService;

    Integer oid,did;
    User user=null;

    @ModelAttribute
    public void comment(HttpServletRequest request){
        user = (User)request.getAttribute("user");
        UserInfo userInfo=userService.getUserInfo(user.getId());
        oid=userInfo.getOid();
        did=userInfo.getDid();
    }

    @RequestMapping("/getDepartments")
    public RetJson getDepartmentsByOid(){
        return RetJson.succcess("Departments",departmentService.getDepartmentList(oid));
    }

    @RequestMapping("/getDepartment")
    public RetJson getDepartment(Integer did){
        Department department=departmentService.getDepartment(oid,did);
        if (department==null){
            return RetJson.fail(-1,"没有这个部门");
        }
        return RetJson.succcess("Departments",department);
    }

    @RequestMapping("/deleteDepartment")
    public RetJson deleteDepartment(Integer did){
        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        try{
            if(departmentService.deleteDepartment(did,oid)){
                return RetJson.succcess(null);
            }
        }catch (Exception e){
            return  RetJson.fail(-1,"该部门的员工不为空");
        }
        return  RetJson.fail(-1,"删除失败！");
    }

    @RequestMapping("/insertDepartment")
    public RetJson insertDepartment(Department department){
        if (!ValidatedUtil.validate(department)){
            return RetJson.fail(-1,"参数错误");
        }

        if (department.getAdmin()!=null){
            departmentService.addAdmin(oid,did,department.getAdmin());
        }
        department.setOid(oid);
        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        if(departmentService.addDepartment(department)){
            return RetJson.succcess("did",department.getDepartmentId());
        }
        return RetJson.fail(-1,"插入失败！");
    }


    @RequestMapping("/uploadDepartmentImage")
    public RetJson uploadDepartmentImage(Integer did,@RequestParam("photo") MultipartFile multipartFile){
        if (multipartFile.getSize()>MAX_SIZE){
            return RetJson.fail(-1,"图片大小不能超过5m");
        }
        Integer oid=userService.getUserInfo(user.getId()).getOid();
        boolean b=departmentService.uploadDepartmentImage(oid,did,multipartFile);
        if (!b){
            return RetJson.fail(-1,"上传失败");
        }
        return RetJson.succcess(null);
    }

    @RequestMapping("/alterDepartment")
    public RetJson alterDepartment(Department department){
        if (!ValidatedUtil.validate(department)){
            return RetJson.fail(-1,"参数错误");
        }

        department.setOid(oid);
        if (department.getAdmin()!=null){
            departmentService.addAdmin(oid,did,department.getAdmin());
        }

        department.setOid(oid);
        if (!departmentService.alterDepartment(department)){
            return RetJson.fail(-1,"修改失败!");
        }
        return RetJson.succcess(null);
    }

    @RequestMapping("/getAllUserInfoByDid")
    public RetJson getAllUserByDid(@RequestParam("did")Integer did){
        List<UserInfo> list=userService.getUserInfoListByDid(oid,did);
        Map<String,Object> map=new HashMap<>();
        Department department=departmentService.getDepartment(oid,did);
        map.put("list",list);
        map.put("admin",department.getAdmin());
        return RetJson.succcess(map);
    }

}
