package com.gis.omp.cos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.gis.omp.account"})
@SpringBootApplication
public class CosApplication {

    public static void main(String[] args) {
        SpringApplication.run(CosApplication.class, args);
    }
}
