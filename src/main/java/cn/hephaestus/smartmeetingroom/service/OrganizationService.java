package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;

public interface OrganizationService {
    public boolean addOne(OrganizationInfo organizationInfo);

    public boolean alterOne(OrganizationInfo organizationInfo);

    public OrganizationInfo getOne(Integer oid);

    public OrganizationInfo[] getOrganizationInfos();
}
