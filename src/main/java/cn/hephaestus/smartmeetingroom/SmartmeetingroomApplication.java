package cn.hephaestus.smartmeetingroom;

import cn.hephaestus.smartmeetingroom.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("cn.hephaestus.smartmeetingroom.mapper")
public class SmartmeetingroomApplication {

    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(SmartmeetingroomApplication.class, args);
        SpringUtil.setApplicationContext(app);
    }
}
