package cn.hephaestus.smartmeetingroom.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Department {

    private Integer departmentId;

    private Integer oid;            //组织id

    private String departmentName;

}
