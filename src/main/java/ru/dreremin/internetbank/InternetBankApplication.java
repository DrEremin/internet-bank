package ru.dreremin.internetbank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class InternetBankApplication {

    public static void main(String[] args) {

        log.info("Application started");
        SpringApplication.run(InternetBankApplication.class, args);
        log.info("Application finished");
    }

}
