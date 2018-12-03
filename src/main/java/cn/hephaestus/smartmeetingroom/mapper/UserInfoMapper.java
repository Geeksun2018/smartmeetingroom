package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    public UserInfo getUserInfoById(@Param("id")Integer id);

    //@Insert({"insert into user(username,password,salt,role) values(#{user.username},#{user.password},#{user.salt},#{user.role})"})

    @Update({"update user_info set phone_num=#{userInfo.phoneNum},email=#{userInfo.email},image_path=#{userInfo.imagePath},sex=#{userInfo.sex} where id=#{userInfo.id}"})
    public void alterUserInfo(@Param("id")Integer id,@Param("userInfo")UserInfo userInfo);

}
