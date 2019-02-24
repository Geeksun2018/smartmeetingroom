package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.FileManagementMapper;
import cn.hephaestus.smartmeetingroom.model.FileManagement;
import cn.hephaestus.smartmeetingroom.service.FileManagementService;
import cn.hephaestus.smartmeetingroom.utils.COSUtils;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileManagementServiceImpl implements FileManagementService {
    @Autowired
    FileManagementMapper fileManagementMapper;

    @Override
    public Integer saveFile(FileManagement fileManagement){
        return fileManagementMapper.insertFile(fileManagement);
    }

    @Override
    public String uploadFileAndGetUrl(MultipartFile multipartFile, String key){
        InputStream in = null;
        try {
            in = multipartFile.getInputStream();
            String url = COSUtils.addFile(key,in);
            return url;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                in.close();
            }catch (IOException e){
                LogUtils.getExceptionLogger().error(e.toString());
            }
        }
        return null;
    }

    @Override
    public boolean updateFile(FileManagement fileManagement) {
        return fileManagementMapper.updateFile(fileManagement);
    }

    @Override
    public FileManagement getFile(Integer id) {
        return fileManagementMapper.getFileById(id);
    }

    @Override
    public FileManagement[] getMeetingFiles(Integer mid){
        return fileManagementMapper.getMeetingFiles(mid);
    }

    @Override
    public boolean deleteFile(Integer id) {
        return fileManagementMapper.deleteFileByFileId(id);
    }

    @Override
    public FileManagement[] getOrgFiles(Integer oid) {

        return fileManagementMapper.getOrgFiles(oid);
    }

}
