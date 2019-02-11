package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    @Results(id="userMap",value={
            @Result(column = "reserve_jurisdiction",property = "reserveJurisdiction"),
    })
    public User getUserByUserName(@Param("username") String username);

    @Select("select * from user where id=#{id}")
    @ResultMap(value = "userMap")
    public User getUserByUserId(@Param("id") Integer id);


    @Insert({"insert into user(username,password,salt,role) values(#{username},#{password},#{salt},#{role})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void register(User user);

    @Update("update user set role=#{role} where id=#{id} ")
    public boolean alterRole(@Param("id") Integer id,@Param("role") Integer role);

    @Select("select reserve_jurisdiction from user where id=#{id}")
    public int getReserveJurisdiction(@Param("id") Integer id);

    @Update("update user set reserve_jurisdiction=#{jurisdiction} where id=#{id}")
    public boolean alterReserveJurisdiction(@Param("jurisdiction") Integer jurisdiction,@Param("id")Integer id);
}
