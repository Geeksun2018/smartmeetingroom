package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;

/**
 * 表示一个组织
 */
@Getter
@Setter
@ToString
public class OrganizationInfo {
    public int id;
    public int rootId;//根用户id
    public String orgName;//组织名称
    public String address;//企业地址
    @Email
    public String email;//企业邮箱
    public String introduction;//组织介绍
    public String meetingRoomCount;//会议室的数量
    public String userConut;//该企业用户数量
}
