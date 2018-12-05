package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    @Results({
            @Result(column = "phone_num",property = "phoneNum"),
            @Result(column = "image_path",property = "imagePath")
    })
    public UserInfo getUserInfoById(@Param("id")Integer id);

    @Update({"update user_info set phone_num=#{userInfo.phoneNum},email=#{userInfo.email},sex=#{userInfo.sex} where id=#{userInfo.id}"})
    public void alterUserInfo(@Param("id")Integer id,@Param("userInfo")UserInfo userInfo);

    @Update("update user_info set image_path=#{imagePath} where id=#{id}")
    public Boolean alterHeadPortrait(@Param("id") Integer id,@Param("imagePath") String url);
}
