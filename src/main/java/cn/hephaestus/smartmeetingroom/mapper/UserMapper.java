package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User getUserByUserName(@Param("username") String username);

    @Select("select * from user where id=#{id}")
    public User getUserByUserId(@Param("id") Integer id);


    @Insert({"insert into user(username,password,salt,role) values(#{username},#{password},#{salt},#{role})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void register(User user);

    @Update("update user set role=#{role} where id=#{id} ")
    public User alterRole(@Param("id") Integer id,@Param("role") Integer role);
}
