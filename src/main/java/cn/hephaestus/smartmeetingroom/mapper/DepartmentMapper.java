package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.Department;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DepartmentMapper {
    @Insert("insert into department(department_name,oid,introduction,admin) values(#{departmentName},#{oid},#{introduction},#{admin})")
    @Options(useGeneratedKeys = true,keyProperty = "departmentId",keyColumn = "department_id")
    public boolean addDepartment(Department department);

    @Update("update department set image=#{image} where department_id=#{did} and oid=#{oid}")
    public boolean alterDepartmentImage(@Param("image") String image,@Param("did") Integer did,@Param("oid") Integer oid);

    @Delete({"delete from department where department_id=#{did} and oid=#{oid}"})
    public boolean deleteDepartment(@Param("did") Integer did,@Param("oid") Integer oid);

    @Update("update department set department_name=#{departmentName},introduction=#{introduction},admin=#{admin} where department_id=#{departmentId} and oid=#{oid}")
    public boolean alterDerpartment(Department department);

    @Select("select * from department where department_id=#{did} and oid=#{oid}")
    @Results(id="departmentMap",value={
        @Result(column = "department_id",property = "departmentId"),
        @Result(column = "department_name",property = "departmentName")
    })
    public Department getDepartment(@Param("oid")Integer oid,@Param("did") Integer did);

    @Select("select * from department where oid=#{oid}")
    @ResultMap(value = "departmentMap")
    public Department[] getDepartmentList(Integer oid);

    @Update("update user_info set did = #{did} where id = #{userId}")
    public Boolean setDepartment(@Param("did") Integer did,@Param("userId") Integer userId);

    @Update("update department set admin=#{admin} where oid=#{oid} and department_id=#{did}")
    public Boolean setAdmin(@Param("oid")Integer oid,@Param("did")Integer did,@Param("admin")String admin);

    @Select("select admin from department where department_id=#{did} and oid=#{oid}")
    public String getAdmin(@Param("oid") Integer oid,@Param("did") Integer did);
}
