package com.example.springboot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@MapperScan("com.example.springboot.mapper")
public class Application {

public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("后台接口地址：http://localhost:9090/swagger-ui/index.html");
        System.out.println("平台地址：http://localhost:7000/front/");
        System.out.println("管理系统地址：http://localhost:7000/home");
        System.out.println("前台页面地址：http://localhost:7000/front/home");
    }

}
