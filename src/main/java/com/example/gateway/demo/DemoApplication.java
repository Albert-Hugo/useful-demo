package com.example.gateway.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    @RequestMapping("test")
    public String test() throws InterruptedException {
        Thread.sleep(1000 * 5);
        return "I am ok";
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
