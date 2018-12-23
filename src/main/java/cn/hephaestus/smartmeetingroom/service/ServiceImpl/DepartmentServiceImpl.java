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
    public boolean addDepartment(Department department) {
        return departmentMapper.addDepartment(department);
    }

    @Override
    public boolean alterDepartment(Department department) {
        return departmentMapper.alterDerpartment(department);
    }

    @Override
    public boolean deleteDepartment(Integer did, Integer oid) {
        return departmentMapper.deleteDepartment(did,oid);
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
