package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User getUserByUserName(@Param("username") String username);

    @Insert("insert into user(username,password,salt,role) values(#{user.userName},#{user.password},#{user.salt},#{user.role})")
    @SelectKey(statement = "select id from user where username = #{user.userName}",keyProperty = "id",before = false,resultType = Integer.class)
    public int register(@Param("user")User user);

}
