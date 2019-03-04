package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.DepartmentMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserInfoMapper;
import cn.hephaestus.smartmeetingroom.mapper.UserMapper;
import cn.hephaestus.smartmeetingroom.model.Department;
import cn.hephaestus.smartmeetingroom.model.UserInfo;
import cn.hephaestus.smartmeetingroom.service.DepartmentService;
import cn.hephaestus.smartmeetingroom.utils.COSUtils;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

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
        //删除前必须把所有员工的部门都改成另外的部门！
        return departmentMapper.deleteDepartment(did,oid);
    }

    @Override
    public Department getDepartment(Integer oid,Integer did) {
        return departmentMapper.getDepartment(oid,did);
    }

    @Override
    public Department[] getDepartmentList(Integer oid) {
        return departmentMapper.getDepartmentList(oid);
    }

    @Override
    public boolean uploadDepartmentImage(Integer oid,Integer did,MultipartFile multipartFile) {
        InputStream inputStream=null;
        try {
            inputStream=multipartFile.getInputStream();
            String imagePath=COSUtils.addFile("/departpent/"+oid+"_"+did,inputStream);
            return departmentMapper.alterDepartmentImage(imagePath,did,oid);
        }catch (Exception e){
            LogUtils.getExceptionLogger().error(e.toString());
            return false;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean addAdmin(Integer oid,Integer did,String admin) {
        Set<Integer> oldSet=new HashSet<>();
        Set<Integer> newSet=new HashSet<>();
        StringBuilder stringBuilder=null;
        //先查出该部门的管理员
        Department department=departmentMapper.getDepartment(oid,did);
        try {
            if (department.getAdmin()!=null){
                String temp=department.getAdmin();
                String[] arr=temp.split("-");
                for (int i=0;i<arr.length;i++){
                    oldSet.add(Integer.valueOf(arr[i]));
                }
            }

            String[] arr=admin.split("-");
            for (int i=0;i<arr.length;i++){
                System.out.println("");
                newSet.add(Integer.valueOf(arr[i]));
            }
            //遍历老的
            for (Integer i:oldSet){
                if (!newSet.contains(i)){
                    userMapper.alterRole(i,0);//取消权限
                }
            }
            stringBuilder=new StringBuilder();
            //遍历新的
            for (Integer i:newSet){
                UserInfo userInfo=userInfoMapper.getUserInfoById(i);
                if (userInfo==null||userInfo.getDid()!=did){
                    continue;
                }
                if (userInfo.getDid()!=did){
                    continue;
                }
                userMapper.alterRole(i,1);
                stringBuilder.append(i+"-");
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        departmentMapper.setAdmin(oid,did,stringBuilder.toString());
        return true;
    }

    @Override
    public Set<Integer> getAdmin(Integer oid, Integer did) {
        Set<Integer> set = new HashSet<>();
        String adminStr = departmentMapper.getAdmin(oid,did);
        String[] uid = adminStr.split("-");
        for (Integer i=0;i < uid.length;i++){
            set.add(Integer.valueOf(uid[i]));
        }
        return set;
    }

}
