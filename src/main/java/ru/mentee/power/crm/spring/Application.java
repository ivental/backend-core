package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "ru.mentee.power.crm")

public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
