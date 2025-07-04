package com.reggi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class ReggieApplication {
    public static void main(String[] args){
        SpringApplication.run(ReggieApplication.class, args);
        log.info("Application started");
    }
}
