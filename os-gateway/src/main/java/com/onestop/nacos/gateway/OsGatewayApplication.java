package com.onestop.nacos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Clark
 * @version 2021/6/21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OsGatewayApplication.class, args);
    }
}