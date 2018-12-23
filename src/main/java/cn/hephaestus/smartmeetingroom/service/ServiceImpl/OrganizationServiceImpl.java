package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.OrganizationMapper;
import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;
import cn.hephaestus.smartmeetingroom.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    OrganizationMapper organizationMapper;

    @Override
    public boolean addOne(OrganizationInfo organizationInfo) {
        return organizationMapper.addOrganization(organizationInfo);
    }

    @Override
    public boolean alterOne(OrganizationInfo organizationInfo) {
        return organizationMapper.alterOrganization(organizationInfo);
    }

    @Override
    public OrganizationInfo getOne(Integer oid) {
        return organizationMapper.getOrganization(oid);
    }

    @Override
    public OrganizationInfo[] getOrganizationInfos() {
        return organizationMapper.getOrganizations();
    }
}
