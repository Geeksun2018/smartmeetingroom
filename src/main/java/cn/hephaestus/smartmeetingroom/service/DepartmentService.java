package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Department;

public interface DepartmentService {

    public int addDepartment(Department department);

    public void  alterDepartment(Department department);
    //根据部门id与组织id删除该部门
    public  void deleteDepartment(Integer did,Integer oid);

    public Department getDepartment(Integer did);

    //查询组织中的所有部门
    public Department[] getDepartmentList(Integer oid);
}
