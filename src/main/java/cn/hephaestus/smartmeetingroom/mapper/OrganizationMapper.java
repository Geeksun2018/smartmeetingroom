package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrganizationMapper {
//    增加一个企业
    @Insert({"insert into organization(rootId) values(#{rootId})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public boolean addOrganization(OrganizationInfo organizationInfo);

    @Update("update organization set org_name=#{orgName},email=#{email},address=#{address},introduction=#{introduction} where id=#{id}")
    public boolean alterOrganization(OrganizationInfo organizationInfo);

    @Select("select * from organization where id=#{oid}")
    @Results({
            @Result(column = "org_name",property = "orgName")
    })
    public OrganizationInfo getOrganization(Integer oid);


    @Select("select * from organization")
    @Results({
            @Result(column = "org_name",property = "orgName")
    })
    public OrganizationInfo[] getOrganizations();
}
