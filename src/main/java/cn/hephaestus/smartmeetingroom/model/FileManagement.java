package cn.hephaestus.smartmeetingroom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileManagement {
    @Null
    private Integer id;

    private String fileName;

    private String type;
    @Null
    private String path;

    private Integer mid;
    @Null
    private Integer uid;
    @Null
    private Integer did;
    @Null
    private Integer oid;
}
