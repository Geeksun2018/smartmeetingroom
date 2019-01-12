package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.model.User;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import cn.hephaestus.smartmeetingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 部门管理
 */
@RestController
public class DepartMentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    UserService userService;

    @RequestMapping("/getDepartments")
    public RetJson getDepartmentsByOid(Integer oid,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(userService.getUserInfo(user.getId()).getOid() != oid){
            return RetJson.fail(-1,"非法操作！");
        }
        return RetJson.succcess("Departments",departmentService.getDepartmentList(oid));
    }

    @RequestMapping("/getDepartment")
    public RetJson getDepartment(Integer did,HttpServletRequest request){
        return RetJson.succcess("Departments",departmentService.getDepartment(did));
    }

    @RequestMapping("/deleteDepartment")
    public RetJson deleteDepartment(Integer did,Integer oid,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        if(departmentService.deleteDepartment(did,oid)){
            return RetJson.succcess(null);
        }
        return  RetJson.fail(-1,"删除失败！");
    }

    @RequestMapping("/insertDepartment")
    public RetJson insertDepartment(Department department,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        if(user.getRole()==0){
            return RetJson.fail(-1,"当前用户没有权限！");
        }
        if(departmentService.addDepartment(department)){
            return RetJson.succcess("did",department.getDepartmentId());
        }
        return RetJson.fail(-1,"插入失败！");
    }

}
