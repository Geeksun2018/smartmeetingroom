package cn.hephaestus.smartmeetingroom;

import cn.hephaestus.smartmeetingroom.model.FileManagement;
import cn.hephaestus.smartmeetingroom.service.FileManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmartmeetingroomApplicationTests {

    @Autowired
    FileManagementService fileManagementService;
    @Test
    public void test(){
        FileManagement fileManagement = new FileManagement();
        fileManagement.setDid(1);
        fileManagement.setOid(5);
        fileManagement.setPath("123455");
        fileManagement.setMid(69);
        fileManagement.setUid(37);
        fileManagement.setType("txt");
        fileManagement.setFileName("12345");
        fileManagement.setId(1);
        fileManagementService.updateFile(fileManagement);
    }
}