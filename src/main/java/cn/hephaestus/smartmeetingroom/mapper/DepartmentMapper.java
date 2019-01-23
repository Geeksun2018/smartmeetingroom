package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Department;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DepartmentMapper {
    @Insert({"insert into department(department_name,oid) values(#{departmentName},#{oid})"})
    @Options(useGeneratedKeys = true,keyProperty = "departmentId",keyColumn = "department_id")
    public boolean addDepartment(Department department);

    @Delete("delete from department where department_id=#{did} and oid=#{oid}")
    public boolean deleteDepartment(@Param("did") Integer did,@Param("oid") Integer oid);

    @Update("update department set department_name=#{department.departmentName},oid=#{department.oid} where department_id=#{department.departmentId} and oid=#{department.oid}")
    public boolean alterDerpartment(@Param("department") Department department);

    @Select("select * from department where department_id=#{did}")
    @Results(id="departmentMap",value={
        @Result(column = "department_id",property = "departmentId"),
        @Result(column = "department_name",property = "departmentName")
    })
    public Department getDepartment(@Param("did") Integer did);

    @Select("select * from department where oid=#{oid}")
    @ResultMap(value = "departmentMap")
    public Department[] getDepartmentList(Integer oid);

    @Update("update user_info set did = #{did} where id = #{userId}")
    public Boolean setDepartment(@Param("did") Integer did,@Param("userId") Integer userId);

    @Select("select id from user_info where did=#{did}")
    public Integer[] getDepartmentStaff(@Param("did") Integer did);
}
