package cn.hephaestus.smartmeetingroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.hephaestus.smartmeetingroom.mapper")
public class SmartmeetingroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartmeetingroomApplication.class, args);
    }
}
