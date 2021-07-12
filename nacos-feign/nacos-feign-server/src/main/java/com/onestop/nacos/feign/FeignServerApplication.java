package com.onestop.nacos.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Clark
 * @version 2021/6/9
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.onestop"})
public class FeignServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignServerApplication.class, args);
    }
}
