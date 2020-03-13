package com.ibu.reminder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReminderApplication.class, args);
        log.info("定时器已启动");
    }

}
