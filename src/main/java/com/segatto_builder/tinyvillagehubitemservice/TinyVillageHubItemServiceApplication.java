package com.segatto_builder.tinyvillagehubitemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties
public class TinyVillageHubItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyVillageHubItemServiceApplication.class, args);
    }

}
