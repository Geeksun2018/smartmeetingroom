package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Department {
    private Integer departmentId;//部门id

    private Integer oid;            //组织id

    @Length(min = 1,max =30)
    private String departmentName;//部门名称

    private String image;//部门图片

    @Length(min = 1,max = 200)
    private String introduction;//部门简介

    private String admin;//部门管理员
}
