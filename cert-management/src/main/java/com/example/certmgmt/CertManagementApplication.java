package com.example.certmgmt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 证书资产管理系统主应用类
 * 
 * @author System
 * @since 1.0.0
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.certmgmt.infrastructure.persistence.mapper")
public class CertManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertManagementApplication.class, args);
    }

}