package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Department;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface DepartmentService {

    public boolean addDepartment(Department department);

    public boolean  alterDepartment(Department department);
    //根据部门id与组织id删除该部门
    public  boolean deleteDepartment(Integer did,Integer oid);

    public Department getDepartment(Integer oid,Integer did);

    //查询组织中的所有部门
    public Department[] getDepartmentList(Integer oid);

    public boolean uploadDepartmentImage(Integer oid,Integer did,MultipartFile multipartFile);

    public boolean addAdmin(Integer oid,Integer did,String admin);

    public Set<Integer> getAdmin(Integer oid, Integer did);

}
