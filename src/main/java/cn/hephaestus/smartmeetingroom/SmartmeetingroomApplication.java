package cn.hephaestus.smartmeetingroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("cn.hephaestus.smartmeetingroom.mapper")
@EnableCaching
public class SmartmeetingroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartmeetingroomApplication.class, args);
    }
}
