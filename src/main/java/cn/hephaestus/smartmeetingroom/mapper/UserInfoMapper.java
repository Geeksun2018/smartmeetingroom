package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    @Results({
            @Result(column = "phone_num",property = "phoneNum"),
            @Result(column = "image_path",property = "imagePath"),
            @Result(column = "nick_name",property = "nickName")
    })
    public UserInfo getUserInfoById(@Param("id")Integer id);

    @Update({"update user_info set phone_num=#{userInfo.phoneNum},email=#{userInfo.email},sex=#{userInfo.sex},name=#{userInfo.name},nick_name=#{userInfo.nickName}" +
            " where id=#{userInfo.id}"})
    public void alterUserInfo(@Param("id")Integer id,@Param("userInfo")UserInfo userInfo);

    @Update("update user_info set image_path=#{imagePath} where id=#{id}")
    public Boolean alterHeadPortrait(@Param("id") Integer id,@Param("imagePath") String url);

    @Update("update user_info set oid=#{oid} where id=#{userId}")
    public boolean setOriganization(@Param("oid") Integer oid,@Param("userId") Integer userId);

    @Update("update user_info set fid=#{fid} where id=#{userId}")
    public boolean setFaceFeatureData(@Param("fid") Integer fid,@Param("userId") Integer userId);
}
