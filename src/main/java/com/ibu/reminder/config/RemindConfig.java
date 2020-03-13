package com.ibu.reminder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("remind")
@PropertySource("classpath:remind.properties")
public class RemindConfig {
    private Integer[] projectId;
}
