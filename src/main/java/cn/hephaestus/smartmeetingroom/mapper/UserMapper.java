package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User getUserByUserName(@Param("username") String username);

    @Insert({"insert into user(username,password,salt,role) values(#{user.username},#{user.password},#{user.salt},#{user.role})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public int register(@Param("user")User user);
}
