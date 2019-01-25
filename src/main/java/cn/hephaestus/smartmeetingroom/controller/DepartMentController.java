package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import cn.hephaestus.smartmeetingroom.utils.ValidatedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/getDepartments")
    public RetJson getDepartmentsByOid(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer oid=userService.getUserInfo(user.getId()).getOid();
        return RetJson.succcess("Departments",departmentService.getDepartmentList(oid));
    }

    @RequestMapping("/getDepartment")
    public RetJson getDepartment(Integer did,HttpServletRequest request){
        Department department=departmentService.getDepartment(did);
        if (department==null){
            return RetJson.fail(-1,"没有这个部门");
        }
        return RetJson.succcess("Departments",department);
    }

    @RequestMapping("/deleteDepartment")
    public RetJson deleteDepartment(Integer did,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        Integer oid=userService.getUserInfo(user.getId()).getOid();
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
    public RetJson insertDepartment(Department department,HttpServletRequest request){
        if (ValidatedUtil.validate(department)){
            return RetJson.fail(-1,"参数错误");
        }
        User user = (User)request.getAttribute("user");

        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        if(departmentService.addDepartment(department)){
            return RetJson.succcess("did",department.getDepartmentId());
        }
        return RetJson.fail(-1,"插入失败！");
    }


    @RequestMapping("/alterDepartment")
    public RetJson alterDepartment(Department department,HttpServletRequest request){
        if (!ValidatedUtil.validate(department)){
            return RetJson.fail(-1,"参数错误");
        }
        User user = (User)request.getAttribute("user");
        Integer oid=userService.getUserInfo(user.getId()).getOid();
        department.setOid(oid);
        if (!departmentService.alterDepartment(department)){
            return RetJson.fail(-1,"修改失败!");
        }
        return RetJson.succcess(null);
    }
}
