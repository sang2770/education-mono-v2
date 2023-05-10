package com.sang.nv.education;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EducationApplication {
    public static void main(String[] args) {
        SpringApplication.run(EducationApplication.class, args);
    }

}
