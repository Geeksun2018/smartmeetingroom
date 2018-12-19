package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.DepartmentMapper;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public int addDepartment(Department department) {
        return departmentMapper.addDepartment(department);
    }

    @Override
    public void alterDepartment(Department department) {
        departmentMapper.alterDerpartment(department);
    }

    @Override
    public void deleteDepartment(Integer did, Integer oid) {
        departmentMapper.deleteDepartment(did,oid);
    }

    @Override
    public Department getDepartment(Integer did) {
        return departmentMapper.getDepartment(did);
    }

    @Override
    public Department[] getDepartmentList(Integer oid) {
        return departmentMapper.getDepartmentList(oid);
    }
}
