package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.FileManagement;
import org.springframework.web.multipart.MultipartFile;

public interface FileManagementService {

    public Integer saveFile(FileManagement fileManagement);

    public String uploadFileAndGetUrl(MultipartFile multipartFile, String key);

    public boolean updateFile(FileManagement fileManagement);

    public FileManagement getFile(Integer id);

    public FileManagement[] getMeetingFiles(Integer mid);

    public boolean deleteFile(Integer id);

    public FileManagement[] getOrgFiles(Integer oid);

}
