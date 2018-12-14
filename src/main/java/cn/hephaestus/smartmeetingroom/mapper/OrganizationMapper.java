package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrganizationMapper {
//    增加一个企业
    @Insert("insert into organization(rootId) values(#{rootId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public boolean addOrganization(OrganizationInfo organizationInfo);

    @Update("update organization set name=#{name},email=#{email},address=#{address},introduction=#{introduction} where id=#{id}")
    public boolean alterOrganization(OrganizationInfo organizationInfo);
}
