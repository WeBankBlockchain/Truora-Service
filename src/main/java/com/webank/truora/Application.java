package com.webank.truora;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class Application implements CommandLineRunner {


    public static void main(String[] args) {
        System.out.println("start running");

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        //预留，供扩展
        log.info("----Console START---");
    }
}
