package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.FileManagement;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileManagementMapper {

    @Insert("insert into file_management(file_name,type,path,mid,uid,did,oid) values(#{fileName},#{type},#{path},#{mid},#{uid},#{did},#{oid})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public int insertFile(FileManagement fileManagement);

    @Delete("delete from file_management where id = #{id}")
    public boolean deleteFileByFileId(@Param("id") Integer id);

    @Update("update file_management set file_name=#{fileName},type=#{type} where id=#{id}")
    public boolean updateFile(FileManagement fileManagement);

    @Select("select * from file_management where id=#{id}")
    @Results({
            @Result(column = "file_name",property = "fileName")
    })
    public FileManagement getFileById(@Param("id") Integer id);

    @Select("select * from file_management where mid=#{mid}")
    @Results({
            @Result(column = "file_name",property = "fileName")
    })
    public FileManagement[] getMeetingFiles(@Param("mid")Integer mid);

    @Select("select * from file_management where oid=#{oid}")
    @Results({
            @Result(column = "file_name",property = "fileName")
    })
    public FileManagement[] getOrgFiles(@Param("oid")Integer oid);
}
